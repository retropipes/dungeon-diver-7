/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.loaders.MusicLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.utilities.InvalidDungeonException;
import com.puttysoftware.fileutils.ZipUtilities;

public class DungeonLoadTask extends Thread {
    // Fields
    private final String filename;
    private final boolean isSavedGame;
    private final JFrame loadFrame;
    private final boolean dungeonProtected;

    // Constructors
    public DungeonLoadTask(final String file, final boolean saved, final boolean protect) {
	JProgressBar loadBar;
	this.filename = file;
	this.isSavedGame = saved;
	this.dungeonProtected = protect;
	this.setName(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_NEW_AG_LOADER_NAME));
	this.loadFrame = new JFrame(
		LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_LOADING));
	this.loadFrame.setIconImage(LogoLoader.getIconLogo());
	loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadFrame.getContentPane().add(loadBar);
	this.loadFrame.setResizable(false);
	this.loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.loadFrame.pack();
    }

    // Methods
    @Override
    public void run() {
	this.loadFrame.setVisible(true);
	final Application app = DungeonDiver7.getApplication();
	if (this.isSavedGame) {
	    app.getGameManager().setSavedGameFlag(true);
	} else {
	    app.getGameManager().setSavedGameFlag(false);
	}
	try {
	    final File dungeonFile = new File(this.filename);
	    final File tempLock = new File(AbstractDungeon.getDungeonTempFolder() + "lock.tmp");
	    AbstractDungeon gameDungeon = DungeonManager.createDungeon();
	    if (this.dungeonProtected) {
		// Attempt to unprotect the file
		DungeonProtectionWrapper.unprotect(dungeonFile, tempLock);
		try {
		    ZipUtilities.unzipDirectory(tempLock, new File(gameDungeon.getBasePath()));
		    app.getDungeonManager().setDungeonProtected(true);
		} catch (final ZipException ze) {
		    CommonDialogs.showErrorDialog(
			    LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
				    LocaleConstants.ERROR_STRING_BAD_PROTECTION_KEY),
			    LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
				    LocaleConstants.ERROR_STRING_PROTECTION));
		    app.getDungeonManager().handleDeferredSuccess(false);
		    return;
		} finally {
		    tempLock.delete();
		}
	    } else {
		ZipUtilities.unzipDirectory(dungeonFile, new File(gameDungeon.getBasePath()));
		app.getDungeonManager().setDungeonProtected(false);
	    }
	    // Set prefix handler
	    gameDungeon.setPrefixHandler(new DungeonFilePrefixHandler());
	    // Set suffix handler
	    if (this.isSavedGame) {
		gameDungeon.setSuffixHandler(new DungeonFileSuffixHandler());
	    } else {
		gameDungeon.setSuffixHandler(null);
	    }
	    gameDungeon = gameDungeon.readDungeon();
	    if (gameDungeon == null) {
		throw new InvalidDungeonException(LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
			LocaleConstants.ERROR_STRING_UNKNOWN_OBJECT));
	    }
	    app.getDungeonManager().setDungeon(gameDungeon);
	    final boolean playerExists = gameDungeon.doesPlayerExist(0);
	    if (playerExists) {
		app.getGameManager().getPlayerManager().resetPlayerLocation();
	    }
	    if (!this.isSavedGame) {
		gameDungeon.save();
	    }
	    // Final cleanup
	    final String lum = app.getDungeonManager().getLastUsedDungeon();
	    final String lug = app.getDungeonManager().getLastUsedGame();
	    app.getDungeonManager().clearLastUsedFilenames();
	    if (this.isSavedGame) {
		app.getDungeonManager().setLastUsedGame(lug);
	    } else {
		app.getDungeonManager().setLastUsedDungeon(lum);
	    }
	    app.getEditor().dungeonChanged();
	    MusicLoader.dungeonChanged();
	    if (this.isSavedGame) {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_GAME_LOADING_SUCCESS));
	    } else {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_ARENA_LOADING_SUCCESS));
	    }
	    app.getDungeonManager().handleDeferredSuccess(true);
	} catch (final FileNotFoundException fnfe) {
	    if (this.isSavedGame) {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_GAME_LOADING_FAILED));
	    } else {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_ARENA_LOADING_FAILED));
	    }
	    app.getDungeonManager().handleDeferredSuccess(false);
	} catch (final ProtectionCancelException pce) {
	    app.getDungeonManager().handleDeferredSuccess(false);
	} catch (final IOException ie) {
	    if (this.isSavedGame) {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_GAME_LOADING_FAILED));
	    } else {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_ARENA_LOADING_FAILED));
	    }
	    DungeonDiver7.getErrorLoggerDirectly().logWarning(ie);
	    app.getDungeonManager().handleDeferredSuccess(false);
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	} finally {
	    this.loadFrame.setVisible(false);
	}
    }
}
