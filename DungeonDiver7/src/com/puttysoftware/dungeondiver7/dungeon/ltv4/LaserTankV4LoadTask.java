/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.ltv4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.loader.MusicLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.manager.dungeon.DungeonManager;

public class LaserTankV4LoadTask extends Thread {
    // Fields
    private final String filename;
    private final MainWindow mainWindow;
    private final JPanel loadContent;

    // Constructors
    public LaserTankV4LoadTask(final String file) {
	JProgressBar loadBar;
	this.filename = file;
	this.setName(Strings.untranslated(Untranslated.FILE_LOADER_OLD_NAME));
	this.mainWindow = MainWindow.mainWindow();
	this.mainWindow.setSystemIcon(LogoLoader.getIconLogo());
	loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadContent = new JPanel();
	this.loadContent.add(loadBar);
    }

    // Methods
    @Override
    public void run() {
	this.mainWindow.setAndSave(this.loadContent, Strings.dialog(DialogString.LOADING));
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic().setSavedGameFlag(false);
	try (var dungeonFile = new FileInputStream(this.filename)) {
	    final var gameDungeon = DungeonManager.createDungeon();
	    LaserTankV4File.loadOldFile(gameDungeon, dungeonFile);
	    dungeonFile.close();
	    app.getDungeonManager().setDungeon(gameDungeon);
	    final var playerExists = gameDungeon.doesPlayerExist(0);
	    if (playerExists) {
		app.getGameLogic().getPlayerManager().resetPlayerLocation();
	    }
	    gameDungeon.save();
	    // Final cleanup
	    final var lum = app.getDungeonManager().getLastUsedDungeon();
	    app.getDungeonManager().clearLastUsedFilenames();
	    app.getDungeonManager().setLastUsedDungeon(lum);
	    app.updateLevelInfoList();
	    app.getEditor().dungeonChanged();
	    MusicLoader.dungeonChanged();
	    CommonDialogs.showDialog(Strings.dialog(DialogString.DUNGEON_LOADING_SUCCESS));
	    app.getDungeonManager().handleDeferredSuccess(true, false, null);
	} catch (final FileNotFoundException fnfe) {
	    CommonDialogs.showDialog(Strings.dialog(DialogString.DUNGEON_LOADING_FAILED));
	    app.getDungeonManager().handleDeferredSuccess(false, false, null);
	} catch (final IOException ie) {
	    CommonDialogs.showDialog(ie.getMessage());
	    app.getDungeonManager().handleDeferredSuccess(false, false, null);
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	} finally {
	    this.mainWindow.restoreSaved();
	}
    }
}
