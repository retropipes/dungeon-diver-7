/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.diane.fileio.utility.ZipUtilities;

public class GameSaveTask extends Thread {
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
            final var tempLock = new File(AbstractDungeon.getDungeonTempFolder() + "lock.tmp");
            // Set prefix handler
            app.getDungeonManager().getDungeon().setPrefixHandler(new PrefixHandler());
            // Set suffix handler
            app.getDungeonManager().getDungeon().setSuffixHandler(new SuffixHandler());
            app.getDungeonManager().getDungeon().writeDungeon();
            ZipUtilities.zipDirectory(new File(app.getDungeonManager().getDungeon().getBasePath()), tempLock);
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
}
