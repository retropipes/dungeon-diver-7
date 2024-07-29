/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileNotFoundException;

import org.retropipes.diane.fileio.utility.ZipUtilities;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public class DungeonSaveTask extends Thread {
    private static boolean hasExtension(final String s) {
	String ext = null;
	final var i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i + 1).toLowerCase();
	}
	if (ext == null) {
	    return false;
	}
	return true;
    }

    // Fields
    private String filename;
    private final boolean saveProtected;
    private final boolean isSavedGame;

    // Constructors
    public DungeonSaveTask(final String file, final boolean saved, final boolean protect) {
	this.filename = file;
	this.isSavedGame = saved;
	this.saveProtected = protect;
	this.setName(Strings.untranslated(Untranslated.FILE_SAVER_NEW_NAME));
    }

    @Override
    public void run() {
	final var app = DungeonDiver7.getStuffBag();
	var success = true;
	// filename check
	final var hasExtension = DungeonSaveTask.hasExtension(this.filename);
	if (!hasExtension) {
	    if (this.isSavedGame) {
		this.filename += Strings.fileExtension(FileExtension.SUSPEND);
	    } else {
		this.filename += Strings.fileExtension(FileExtension.DUNGEON);
	    }
	}
	final var dungeonFile = new File(this.filename);
	final var tempLock = new File(AbstractDungeon.getDungeonTempFolder() + "lock.tmp");
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
		CommonDialogs.showDialog(Strings.dialog(DialogString.GAME_SAVING_FAILED));
	    } else {
		CommonDialogs.showDialog(Strings.dialog(DialogString.DUNGEON_SAVING_FAILED));
	    }
	    success = false;
	} catch (final ProtectionCancelException pce) {
	    success = false;
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
	if (this.isSavedGame) {
	    DungeonDiver7.getStuffBag().showMessage(Strings.dialog(DialogString.GAME_SUSPENDED));
	} else {
	    DungeonDiver7.getStuffBag().showMessage(Strings.dialog(DialogString.DUNGEON_SAVED));
	}
	app.getDungeonManager().handleDeferredSuccess(success, false, null);
    }
}
