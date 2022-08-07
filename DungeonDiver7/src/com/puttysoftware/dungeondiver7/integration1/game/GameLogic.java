/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.game;

import javax.swing.JFrame;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
import com.puttysoftware.dungeondiver7.integration1.dungeon.GenerateDungeonTask;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public final class GameLogic {
    // Fields
    private boolean savedGameFlag;
    private final GameViewingWindowManager vwMgr;
    private boolean stateChanged;
    private final GameGUI gui;
    private final MovementTask mt;

    // Constructors
    public GameLogic() {
	this.vwMgr = new GameViewingWindowManager();
	this.gui = new GameGUI();
	this.mt = new MovementTask(this.vwMgr, this.gui);
	this.mt.start();
	this.savedGameFlag = false;
	this.stateChanged = true;
    }

    // Methods
    public boolean newGame() {
	final JFrame owner = Integration1.getApplication().getOutputFrame();
	if (this.savedGameFlag) {
	    if (PartyManager.getParty() != null) {
		return true;
	    } else {
		return PartyManager.createParty(owner);
	    }
	} else {
	    return PartyManager.createParty(owner);
	}
    }

    public void enableEvents() {
	this.mt.fireStepActions();
	this.gui.enableEvents();
    }

    public void disableEvents() {
	this.gui.disableEvents();
    }

    public void stopMovement() {
	this.mt.stopMovement();
    }

    public void viewingWindowSizeChanged() {
	this.gui.viewingWindowSizeChanged();
	this.resetViewingWindow();
    }

    public void stateChanged() {
	this.stateChanged = true;
    }

    public GameViewingWindowManager getViewManager() {
	return this.vwMgr;
    }

    public void setSavedGameFlag(final boolean value) {
	this.savedGameFlag = value;
    }

    public void setStatusMessage(final String msg) {
	this.gui.setStatusMessage(msg);
    }

    public void updatePositionRelative(final int dirX, final int dirY) {
	this.mt.moveRelative(dirX, dirY);
    }

    public void updatePositionAbsolute(final int x, final int y) {
	this.mt.moveAbsolute(x, y);
    }

    public void redrawDungeon() {
	this.gui.redrawDungeon();
    }

    public void resetViewingWindowAndPlayerLocation() {
	GameLogic.resetPlayerLocation();
	this.resetViewingWindow();
    }

    public void resetViewingWindow() {
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	if (m != null && this.vwMgr != null) {
	    this.vwMgr.setViewingWindowLocationX(m.getPlayerLocationY() - GameViewingWindowManager.getOffsetFactorX());
	    this.vwMgr.setViewingWindowLocationY(m.getPlayerLocationX() - GameViewingWindowManager.getOffsetFactorY());
	}
    }

    public static void resetPlayerLocation() {
	GameLogic.resetPlayerLocation(0);
    }

    public static void resetPlayerLocation(final int level) {
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	if (m != null) {
	    m.switchLevel(level);
	    m.setPlayerToStart();
	}
    }

    public void goToLevelOffset(final int level) {
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	final boolean levelExists = m.doesLevelExistOffset(level);
	this.stopMovement();
	if (levelExists) {
	    new LevelLoadTask(level).start();
	} else {
	    new GenerateDungeonTask(false).start();
	}
    }

    public void exitGame() {
	this.stateChanged = true;
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	// Restore the maze
	m.restore();
	m.resetVisibleSquares();
	final boolean playerExists = m.doesPlayerExist();
	if (playerExists) {
	    this.resetViewingWindowAndPlayerLocation();
	} else {
	    app.getDungeonManager().setLoaded(false);
	}
	// Reset saved game flag
	this.savedGameFlag = false;
	app.getDungeonManager().setDirty(false);
	// Exit game
	this.hideOutput();
	app.getGUIManager().showGUI();
    }

    public JFrame getOutputFrame() {
	return this.gui.getOutputFrame();
    }

    public static void decay() {
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	m.setCell(new Empty(), m.getPlayerLocationX(), m.getPlayerLocationY(), DungeonConstants.LAYER_LOWER_OBJECTS);
    }

    public static void morph(final AbstractDungeonObject morphInto) {
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	m.setCell(morphInto, m.getPlayerLocationX(), m.getPlayerLocationY(), morphInto.getLayer());
    }

    public void keepNextMessage() {
	this.gui.keepNextMessage();
    }

    public void playDungeon() {
	final Application app = Integration1.getApplication();
	final CurrentDungeon m = app.getDungeonManager().getDungeon();
	if (app.getDungeonManager().getLoaded()) {
	    this.gui.initViewManager();
	    app.getGUIManager().hideGUI();
	    if (this.stateChanged) {
		// Initialize only if the maze state has changed
		app.getDungeonManager().getDungeon().switchLevel(app.getDungeonManager().getDungeon().getStartLevel());
		this.stateChanged = false;
	    }
	    // Make sure message area is attached to the border pane
	    this.gui.updateGameGUI();
	    // Make sure initial area player is in is visible
	    final int px = m.getPlayerLocationX();
	    final int py = m.getPlayerLocationY();
	    m.updateVisibleSquares(px, py);
	    this.showOutput();
	    this.redrawDungeon();
	} else {
	    CommonDialogs.showDialog("No Dungeon Opened");
	}
    }

    public void showOutput() {
	Integration1.getApplication().setMode(Application.STATUS_GAME);
	this.gui.showOutput();
    }

    public void showOutputAndKeepMusic() {
	Integration1.getApplication().setMode(Application.STATUS_GAME);
	this.gui.showOutputAndKeepMusic();
    }

    public void hideOutput() {
	this.stopMovement();
	this.gui.hideOutput();
    }
}
