/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.game;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.utility.ImageColorConstants;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;

public class LevelLoadTask extends Thread {
    // Fields
    private final JFrame loadFrame;
    private final int level;

    // Constructors
    public LevelLoadTask(final int offset) {
	this.level = offset;
	this.setName("Level Loader");
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
	try {
	    this.loadFrame.setVisible(true);
	    final Application app = Integration1.getApplication();
	    final CurrentDungeon gameDungeon = app.getDungeonManager().getDungeon();
	    app.getGameLogic().disableEvents();
	    gameDungeon.switchLevelOffset(this.level);
	    gameDungeon.offsetPlayerLocationW(this.level);
	    PartyManager.getParty().offsetZone(this.level);
	    AbstractDungeonObject
		    .setTemplateColor(ImageColorConstants.getColorForLevel(PartyManager.getParty().getZone()));
	    app.getGameLogic().resetViewingWindow();
	    app.getGameLogic().enableEvents();
	    app.getGameLogic().redrawDungeon();
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	} finally {
	    this.loadFrame.setVisible(false);
	}
    }
}
