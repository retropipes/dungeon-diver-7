/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.dungeon;

import javax.swing.JProgressBar;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.manager.DungeonManager;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Layer;
import org.retropipes.dungeondiver7.locale.Strings;

public class GenerateDungeonTask extends Thread {
    // Fields
    private final MainWindow mainWindow;
    private final MainContent loadContent;
    private final boolean scratch;

    // Constructors
    public GenerateDungeonTask(final boolean startFromScratch) {
	this.scratch = startFromScratch;
	this.mainWindow = MainWindow.mainWindow();
	final var loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.loadContent = this.mainWindow.createContent();
	this.loadContent.add(loadBar);
    }

    @Override
    public void run() {
	try {
	    this.mainWindow.setAndSave(this.loadContent, Strings.dialog(DialogString.GENERATING));
	    final var app = DungeonDiver7.getStuffBag();
	    final var zoneID = PartyManager.getParty().getZone();
	    final var dungeonSize = DungeonDiver7.getDungeonLevelSize(zoneID);
	    var gameDungeon = app.getDungeonManager().getDungeonBase();
	    if (!this.scratch) {
		app.getGame().disableEvents();
	    } else {
		gameDungeon = DungeonManager.createDungeonBase();
		app.getDungeonManager().setDungeonBase(gameDungeon);
	    }
	    gameDungeon.addFixedSizeLevel(dungeonSize, dungeonSize, 1);
	    DungeonGenerator.fillRandomly(gameDungeon);
	    final var rR = new RandomRange(0, dungeonSize - 1);
	    final var rC = new RandomRange(0, dungeonSize - 1);
	    if (this.scratch) {
		int startR, startC;
		do {
		    startR = rR.generate();
		    startC = rC.generate();
		} while (gameDungeon.getCell(startR, startC, 0, Layer.STATUS.ordinal()).isSolid());
		gameDungeon.setStartRow(startR, 0);
		gameDungeon.setStartColumn(startC, 0);
		app.getDungeonManager().setLoaded(true);
		final var playerExists = gameDungeon.doesPlayerExist(0);
		if (playerExists) {
		    gameDungeon.setPlayerToStart();
		    app.getGame().resetViewingWindow();
		}
	    } else {
		int startR, startC;
		do {
		    startR = rR.generate();
		    startC = rC.generate();
		} while (gameDungeon.getCell(startR, startC, 0, Layer.STATUS.ordinal()).isSolid());
		gameDungeon.setPlayerLocationX(startR, 0);
		gameDungeon.setPlayerLocationY(startC, 0);
		PartyManager.getParty().offsetZone(1);
	    }
	    gameDungeon.save();
	    // Final cleanup
	    if (this.scratch) {
		app.getGame().stateChanged();
		app.getGame().playDungeon();
	    } else {
		app.getGame().resetViewingWindow();
		app.getGame().enableEvents();
		app.getGame().redrawDungeon();
	    }
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	} finally {
	    this.mainWindow.restoreSaved();
	}
    }
}
