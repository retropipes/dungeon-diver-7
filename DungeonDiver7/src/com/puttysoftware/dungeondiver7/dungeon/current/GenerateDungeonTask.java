/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.current;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.manager.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.ImageColors;
import com.puttysoftware.diane.random.RandomRange;

public class GenerateDungeonTask extends Thread {
	// Fields
	private final MainWindow mainWindow;
	private final JPanel loadContent;
	private final boolean scratch;

	// Constructors
	public GenerateDungeonTask(final boolean startFromScratch) {
		this.scratch = startFromScratch;
		this.mainWindow = MainWindow.mainWindow();
		final var loadBar = new JProgressBar();
		loadBar.setIndeterminate(true);
		this.loadContent = new JPanel();
		this.loadContent.add(loadBar);
	}

	@Override
	public void run() {
		try {
			this.mainWindow.setAndSave(this.loadContent, Strings.dialog(DialogString.GENERATING));
			final var app = DungeonDiver7.getStuffBag();
			final var zoneID = PartyManager.getParty().getZone();
			final var dungeonSize = DungeonDiver7.getDungeonLevelSize(zoneID);
			var gameDungeon = app.getDungeonManager().getDungeon();
			if (!this.scratch) {
				app.getGameLogic().disableEvents();
			} else {
				gameDungeon = DungeonManager.createDungeon();
				app.getDungeonManager().setDungeon(gameDungeon);
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
				} while (gameDungeon.getCell(startR, startC, 0, DungeonConstants.LAYER_LOWER_OBJECTS).isSolid());
				gameDungeon.setStartRow(startR, 0);
				gameDungeon.setStartColumn(startC, 0);
				app.getDungeonManager().setLoaded(true);
				final var playerExists = gameDungeon.doesPlayerExist(0);
				if (playerExists) {
					gameDungeon.setPlayerToStart();
					app.getGameLogic().resetViewingWindow();
				}
			} else {
				int startR, startC;
				do {
					startR = rR.generate();
					startC = rC.generate();
				} while (gameDungeon.getCell(startR, startC, 0, DungeonConstants.LAYER_LOWER_OBJECTS).isSolid());
				gameDungeon.setPlayerLocationX(startR, 0);
				gameDungeon.setPlayerLocationY(startC, 0);
				PartyManager.getParty().offsetZone(1);
			}
			gameDungeon.save();
			// Final cleanup
			AbstractDungeonObject.setTemplateColor(ImageColors.getColorForLevel(zoneID));
			if (this.scratch) {
				app.getGameLogic().stateChanged();
				app.getGameLogic().playDungeon();
			} else {
				app.getGameLogic().resetViewingWindow();
				app.getGameLogic().enableEvents();
				app.getGameLogic().redrawDungeon();
			}
		} catch (final Throwable t) {
			DungeonDiver7.logError(t);
		} finally {
			this.mainWindow.restoreSaved();
		}
	}
}
