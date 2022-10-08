/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.utility.ImageColors;

public class LevelLoadTask extends Thread {
    // Fields
    private final MainWindow mainWindow;
    private final int level;

    // Constructors
    public LevelLoadTask(final int offset) {
	this.level = offset;
	this.setName("Level Loader");
	this.mainWindow = MainWindow.mainWindow();
	this.mainWindow.setTitle("Loading...");
	this.mainWindow.setIconImage(LogoLoader.getIconLogo());
	final var loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	var loadContent = new JPanel();
	loadContent.add(loadBar);
	this.mainWindow.setAndSaveContent(loadContent);
	this.mainWindow.setResizable(false);
	this.mainWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.mainWindow.pack();
    }

    // Methods
    @Override
    public void run() {
	try {
	    this.mainWindow.setVisible(true);
	    final var app = DungeonDiver7.getStuffBag();
	    final var gameDungeon = app.getDungeonManager().getDungeon();
	    app.getGameLogic().disableEvents();
	    gameDungeon.switchLevelOffset(this.level);
	    PartyManager.getParty().offsetZone(this.level);
	    AbstractDungeonObject.setTemplateColor(ImageColors.getColorForLevel(PartyManager.getParty().getZone()));
	    app.getGameLogic().resetViewingWindow();
	    app.getGameLogic().enableEvents();
	    app.getGameLogic().redrawDungeon();
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	} finally {
	    this.mainWindow.setVisible(false);
	}
    }
}
