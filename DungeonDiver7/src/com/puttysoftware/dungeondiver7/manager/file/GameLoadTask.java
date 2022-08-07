/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.utility.ImageColorConstants;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.VersionException;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.manager.dungeon.PrefixHandler;
import com.puttysoftware.dungeondiver7.manager.dungeon.SuffixHandler;
import com.puttysoftware.fileutils.ZipUtilities;

public class GameLoadTask extends Thread {
    // Fields
    private final String filename;
    private final JFrame loadFrame;

    // Constructors
    public GameLoadTask(final String file) {
	this.filename = file;
	this.setName("Game Loader");
	this.loadFrame = new JFrame("Loading...");
	this.loadFrame.setIconImage(LogoLoader.getIconLogo());
	final JProgressBar loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadFrame.getContentPane().add(loadBar);
	this.loadFrame.setResizable(false);
	this.loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.loadFrame.pack();
    }

    // Methods
    @Override
    public void run() {
	final String sg = "Game";
	final File mazeFile = new File(this.filename);
	try {
	    this.loadFrame.setVisible(true);
	    final Application app = Integration1.getApplication();
	    int startW;
	    app.getGameLogic().setSavedGameFlag(false);
	    final File tempLock = new File(CurrentDungeon.getDungeonTempFolder() + "lock.tmp");
	    CurrentDungeon gameDungeon = new CurrentDungeon();
	    // Unlock the file
	    GameFileManager.load(mazeFile, tempLock);
	    ZipUtilities.unzipDirectory(tempLock, new File(gameDungeon.getBasePath()));
	    final boolean success = tempLock.delete();
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
	    startW = gameDungeon.getStartLevel();
	    gameDungeon.switchLevel(startW);
	    final boolean playerExists = gameDungeon.doesPlayerExist();
	    if (playerExists) {
		app.getDungeonManager().getDungeon().setPlayerToStart();
		app.getGameLogic().resetViewingWindow();
	    }
	    gameDungeon.save();
	    // Final cleanup
	    app.getGameLogic().stateChanged();
	    AbstractDungeonObject
		    .setTemplateColor(ImageColorConstants.getColorForLevel(PartyManager.getParty().getZone()));
	    app.getDungeonManager().setLoaded(true);
	    CommonDialogs.showDialog(sg + " loaded.");
	    app.getGameLogic().playDungeon();
	    app.getDungeonManager().handleDeferredSuccess(true, false, null);
	} catch (final VersionException ve) {
	    CommonDialogs.showDialog(
		    "Loading the " + sg.toLowerCase() + " failed, due to the format version being unsupported.");
	    Integration1.getApplication().getDungeonManager().handleDeferredSuccess(false, true, mazeFile);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog("Loading the " + sg.toLowerCase()
		    + " failed, probably due to illegal characters in the file name.");
	    Integration1.getApplication().getDungeonManager().handleDeferredSuccess(false, false, null);
	} catch (final IOException ie) {
	    CommonDialogs
		    .showDialog("Loading the " + sg.toLowerCase() + " failed, due to some other type of I/O error.");
	    Integration1.getApplication().getDungeonManager().handleDeferredSuccess(false, false, null);
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	} finally {
	    this.loadFrame.setVisible(false);
	}
    }
}