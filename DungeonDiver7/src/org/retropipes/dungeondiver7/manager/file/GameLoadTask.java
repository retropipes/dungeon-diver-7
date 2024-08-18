/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JProgressBar;

import org.retropipes.diane.fileio.utility.ZipUtilities;
import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.VersionException;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.current.CurrentDungeon;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.utility.ImageColors;

public class GameLoadTask extends Thread {
    // Fields
    private final String filename;
    private final MainWindow mainWindow;
    private final MainContent loadContent;

    // Constructors
    public GameLoadTask(final String file) {
	this.filename = file;
	this.setName("Game Loader");
	this.mainWindow = MainWindow.mainWindow();
	final var loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadContent = this.mainWindow.createContent();
	this.loadContent.add(loadBar);
    }

    @Override
    public void run() {
	final var sg = "Game";
	final var mazeFile = new File(this.filename);
	try {
	    this.mainWindow.setAndSave(this.loadContent, Strings.dialog(DialogString.LOADING));
	    final var app = DungeonDiver7.getStuffBag();
	    int startW;
	    app.getGameLogic().setSavedGameFlag(false);
	    final var tempLock = new File(Dungeon.getDungeonTempFolder() + "lock.tmp");
	    var gameDungeon = new CurrentDungeon();
	    // Unlock the file
	    GameFileManager.load(mazeFile, tempLock);
	    ZipUtilities.unzipDirectory(tempLock, new File(gameDungeon.getBasePath()));
	    final var success = tempLock.delete();
	    if (!success) {
		throw new IOException("Failed to delete temporary file!");
	    }
	    // Set prefix handler
	    gameDungeon.setPrefixHandler(new PrefixHandler());
	    // Set suffix handler
	    gameDungeon.setSuffixHandler(new SuffixHandler());
	    gameDungeon = gameDungeon.readDungeon();
	    if (gameDungeon == null) {
		throw new IOException("Unknown object encountered.");
	    }
	    app.getDungeonManager().setDungeon(gameDungeon);
	    startW = gameDungeon.getStartLevel(0);
	    gameDungeon.switchLevel(startW);
	    final var playerExists = gameDungeon.doesPlayerExist(0);
	    if (playerExists) {
		app.getDungeonManager().getDungeon().setPlayerToStart();
		app.getGameLogic().resetViewingWindow();
	    }
	    gameDungeon.save();
	    // Final cleanup
	    app.getGameLogic().stateChanged();
	    DungeonObject.setTemplateColor(ImageColors.getColorForLevel(PartyManager.getParty().getZone()));
	    app.getDungeonManager().setLoaded(true);
	    CommonDialogs.showDialog(sg + " loaded.");
	    app.getGameLogic().playDungeon();
	    app.getDungeonManager().handleDeferredSuccess(true, false, null);
	} catch (final VersionException ve) {
	    CommonDialogs.showDialog(
		    "Loading the " + sg.toLowerCase() + " failed, due to the format version being unsupported.");
	    DungeonDiver7.getStuffBag().getDungeonManager().handleDeferredSuccess(false, true, mazeFile);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
		    + " failed, probably due to illegal characters in the file name.");
	    DungeonDiver7.getStuffBag().getDungeonManager().handleDeferredSuccess(false, false, null);
	} catch (final IOException ie) {
	    CommonDialogs
		    .showDialog("Loading the " + sg.toLowerCase() + " failed, due to some other type of I/O error.");
	    DungeonDiver7.getStuffBag().getDungeonManager().handleDeferredSuccess(false, false, null);
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	} finally {
	    this.mainWindow.restoreSaved();
	}
    }
}
