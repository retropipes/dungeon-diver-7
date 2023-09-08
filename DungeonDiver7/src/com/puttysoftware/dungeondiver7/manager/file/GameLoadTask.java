/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JProgressBar;

import com.puttysoftware.diane.fileio.utility.ZipUtilities;
import com.puttysoftware.diane.gui.MainContent;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.VersionException;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.utility.ImageColors;

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
	this.loadContent = MainWindow.createContent();
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
	    final var tempLock = new File(AbstractDungeon.getDungeonTempFolder() + "lock.tmp");
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
	    AbstractDungeonObject.setTemplateColor(ImageColors.getColorForLevel(PartyManager.getParty().getZone()));
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
