/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.game;

import javax.swing.JProgressBar;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Strings;

public class LevelLoadTask extends Thread {
    // Fields
    private final MainWindow mainWindow;
    private final MainContent loadContent;
    private final int level;

    // Constructors
    public LevelLoadTask(final int offset) {
	this.level = offset;
	this.setName("Level Loader");
	this.mainWindow = MainWindow.mainWindow();
	final var loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadContent = this.mainWindow.createContent();
	this.loadContent.add(loadBar);
    }

    @Override
    public void run() {
	try {
	    this.mainWindow.setAndSave(this.loadContent, Strings.dialog(DialogString.LOADING));
	    final var app = DungeonDiver7.getStuffBag();
	    final var gameDungeon = app.getDungeonManager().getDungeonBase();
	    app.getGame().disableEvents();
	    gameDungeon.switchLevelOffset(this.level);
	    PartyManager.getParty().offsetZone(this.level);
	    app.getGame().resetViewingWindow();
	    app.getGame().enableEvents();
	    app.getGame().redrawDungeon();
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	} finally {
	    this.mainWindow.restoreSaved();
	}
    }
}
