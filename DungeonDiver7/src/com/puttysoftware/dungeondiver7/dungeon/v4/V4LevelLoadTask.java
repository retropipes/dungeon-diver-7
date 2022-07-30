/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.v4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.loaders.MusicLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;

public class V4LevelLoadTask extends Thread {
    // Fields
    private final String filename;
    private final JFrame loadFrame;

    // Constructors
    public V4LevelLoadTask(final String file) {
	JProgressBar loadBar;
	this.filename = file;
	this.setName(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_OLD_AG_LOADER_NAME));
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
	app.getGameManager().setSavedGameFlag(false);
	try (FileInputStream dungeonFile = new FileInputStream(this.filename)) {
	    final AbstractDungeon gameDungeon = DungeonManager.createDungeon();
	    V4File.loadOldFile(gameDungeon, dungeonFile);
	    dungeonFile.close();
	    app.getDungeonManager().setDungeon(gameDungeon);
	    final boolean playerExists = gameDungeon.doesPlayerExist(0);
	    if (playerExists) {
		app.getGameManager().getPlayerManager().resetPlayerLocation();
	    }
	    gameDungeon.save();
	    // Final cleanup
	    final String lum = app.getDungeonManager().getLastUsedDungeon();
	    app.getDungeonManager().clearLastUsedFilenames();
	    app.getDungeonManager().setLastUsedDungeon(lum);
	    app.updateLevelInfoList();
	    app.getEditor().dungeonChanged();
	    MusicLoader.dungeonChanged();
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		    LocaleConstants.DIALOG_STRING_ARENA_LOADING_SUCCESS));
	    app.getDungeonManager().handleDeferredSuccess(true);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		    LocaleConstants.DIALOG_STRING_ARENA_LOADING_FAILED));
	    app.getDungeonManager().handleDeferredSuccess(false);
	} catch (final IOException ie) {
	    CommonDialogs.showDialog(ie.getMessage());
	    app.getDungeonManager().handleDeferredSuccess(false);
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	} finally {
	    this.loadFrame.setVisible(false);
	}
    }
}
