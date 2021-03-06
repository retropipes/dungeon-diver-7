/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.io.File;
import java.io.FileNotFoundException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.utilities.Extension;
import com.puttysoftware.fileutils.ZipUtilities;

public class DungeonSaveTask extends Thread {
    // Fields
    private String filename;
    private final boolean saveProtected;
    private final boolean isSavedGame;

    // Constructors
    public DungeonSaveTask(final String file, final boolean saved, final boolean protect) {
	this.filename = file;
	this.isSavedGame = saved;
	this.saveProtected = protect;
	this.setName(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_NEW_AG_SAVER_NAME));
    }

    @Override
    public void run() {
	final Application app = DungeonDiver7.getApplication();
	boolean success = true;
	// filename check
	final boolean hasExtension = DungeonSaveTask.hasExtension(this.filename);
	if (!hasExtension) {
	    if (this.isSavedGame) {
		this.filename += Extension.getGameExtensionWithPeriod();
	    } else {
		this.filename += Extension.getDungeonExtensionWithPeriod();
	    }
	}
	final File dungeonFile = new File(this.filename);
	final File tempLock = new File(AbstractDungeon.getDungeonTempFolder() + "lock.tmp");
	try {
	    // Set prefix handler
	    app.getDungeonManager().getDungeon().setPrefixHandler(new DungeonFilePrefixHandler());
	    // Set suffix handler
	    if (this.isSavedGame) {
		app.getDungeonManager().getDungeon().setSuffixHandler(new DungeonFileSuffixHandler());
	    } else {
		app.getDungeonManager().getDungeon().setSuffixHandler(null);
	    }
	    app.getDungeonManager().getDungeon().writeDungeon();
	    if (this.saveProtected) {
		ZipUtilities.zipDirectory(new File(app.getDungeonManager().getDungeon().getBasePath()), tempLock);
		// Protect the dungeon
		DungeonProtectionWrapper.protect(tempLock, dungeonFile);
		tempLock.delete();
		app.getDungeonManager().setDungeonProtected(true);
	    } else {
		ZipUtilities.zipDirectory(new File(app.getDungeonManager().getDungeon().getBasePath()), dungeonFile);
		app.getDungeonManager().setDungeonProtected(false);
	    }
	} catch (final FileNotFoundException fnfe) {
	    if (this.isSavedGame) {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_GAME_SAVING_FAILED));
	    } else {
		CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_ARENA_SAVING_FAILED));
	    }
	    success = false;
	} catch (final ProtectionCancelException pce) {
	    success = false;
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	}
	if (this.isSavedGame) {
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE,
		    LocaleConstants.MESSAGE_STRING_GAME_SAVED));
	} else {
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE,
		    LocaleConstants.MESSAGE_STRING_ARENA_SAVED));
	}
	app.getDungeonManager().handleDeferredSuccess(success);
    }

    private static boolean hasExtension(final String s) {
	String ext = null;
	final int i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i + 1).toLowerCase();
	}
	if (ext == null) {
	    return false;
	} else {
	    return true;
	}
    }
}
