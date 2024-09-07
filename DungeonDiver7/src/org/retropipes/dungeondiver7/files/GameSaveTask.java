/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.retropipes.diane.fileio.utility.ZipUtilities;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.base.DungeonBase;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public class GameSaveTask extends Thread {
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

    // Constructors
    public GameSaveTask(final String file) {
	this.filename = file;
	this.setName("Game Writer");
    }

    @Override
    public void run() {
	var success = true;
	final var sg = "Game";
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    // filename check
	    final var hasExtension = GameSaveTask.hasExtension(this.filename);
	    if (!hasExtension) {
		this.filename += Strings.fileExtension(FileExtension.SUSPEND);
	    }
	    final var mazeFile = new File(this.filename);
	    final var tempLock = new File(DungeonBase.getDungeonTempFolder() + "lock.tmp");
	    // Set prefix handler
	    app.getDungeonManager().getDungeonBase().setPrefixHandler(new PrefixHandler());
	    // Set suffix handler
	    app.getDungeonManager().getDungeonBase().setSuffixHandler(new SuffixHandler());
	    app.getDungeonManager().getDungeonBase().writeDungeon();
	    ZipUtilities.zipDirectory(new File(app.getDungeonManager().getDungeonBase().getBasePath()), tempLock);
	    // Lock the file
	    GameFileManager.save(tempLock, mazeFile);
	    final var delSuccess = tempLock.delete();
	    if (!delSuccess) {
		throw new IOException("Failed to delete temporary file!");
	    }
	    app.showMessage(sg + " saved.");
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog("Writing the " + sg.toLowerCase()
		    + " failed, probably due to illegal characters in the file name.");
	    success = false;
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
	DungeonDiver7.getStuffBag().getDungeonManager().handleDeferredSuccess(success, false, null);
    }
}
