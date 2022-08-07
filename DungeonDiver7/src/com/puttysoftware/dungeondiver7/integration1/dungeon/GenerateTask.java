/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractGameObject;
import com.puttysoftware.dungeondiver7.integration1.dungeon.utility.ImageColorConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.LogoManager;
import com.puttysoftware.randomrange.RandomRange;

public class GenerateTask extends Thread {
    // Fields
    private final JFrame generateFrame;
    private final boolean scratch;

    // Constructors
    public GenerateTask(final boolean startFromScratch) {
	this.scratch = startFromScratch;
	this.setName("Level Generator");
	this.generateFrame = new JFrame("Generating...");
	this.generateFrame.setIconImage(LogoManager.getIconLogo());
	final JProgressBar loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	this.generateFrame.getContentPane().add(loadBar);
	this.generateFrame.setResizable(false);
	this.generateFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.generateFrame.pack();
    }

    // Methods
    @Override
    public void run() {
	try {
	    this.generateFrame.setVisible(true);
	    final Application app = Integration1.getApplication();
	    int zoneID = PartyManager.getParty().getZone();
	    int dungeonSize = DungeonDiver7.getDungeonLevelSize(zoneID);
	    Dungeon gameDungeon = app.getDungeonManager().getDungeon();
	    if (!this.scratch) {
		app.getGameLogic().disableEvents();
	    } else {
		gameDungeon = new Dungeon();
		app.getDungeonManager().setDungeon(gameDungeon);
	    }
	    gameDungeon.addLevel(dungeonSize, dungeonSize);
	    gameDungeon.fillLevelRandomly();
	    final RandomRange rR = new RandomRange(0, dungeonSize - 1);
	    final RandomRange rC = new RandomRange(0, dungeonSize - 1);
	    if (this.scratch) {
		int startR, startC;
		do {
		    startR = rR.generate();
		    startC = rC.generate();
		} while (gameDungeon.getCell(startR, startC, DungeonConstants.LAYER_OBJECT).isSolid());
		gameDungeon.setStartRow(startR);
		gameDungeon.setStartColumn(startC);
		app.getDungeonManager().setLoaded(true);
		final boolean playerExists = gameDungeon.doesPlayerExist();
		if (playerExists) {
		    gameDungeon.setPlayerToStart();
		    app.getGameLogic().resetViewingWindow();
		}
	    } else {
		int startR, startC;
		do {
		    startR = rR.generate();
		    startC = rC.generate();
		} while (gameDungeon.getCell(startR, startC, DungeonConstants.LAYER_OBJECT).isSolid());
		gameDungeon.setPlayerLocationX(startR);
		gameDungeon.setPlayerLocationY(startC);
		PartyManager.getParty().offsetZone(1);
	    }
	    gameDungeon.save();
	    // Final cleanup
	    AbstractGameObject.setTemplateColor(ImageColorConstants.getColorForLevel(zoneID));
	    if (this.scratch) {
		app.getGameLogic().stateChanged();
		app.getGameLogic().playDungeon();
	    } else {
		app.getGameLogic().resetViewingWindow();
		app.getGameLogic().enableEvents();
		app.getGameLogic().redrawDungeon();
	    }
	} catch (final Throwable t) {
	    DungeonDiver7.getErrorLogger().logError(t);
	} finally {
	    this.generateFrame.setVisible(false);
	}
    }
}
