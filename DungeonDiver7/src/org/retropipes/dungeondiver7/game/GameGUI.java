/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.retropipes.diane.LocaleUtils;
import org.retropipes.diane.asset.image.ImageCompositor;
import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.diane.drawgrid.DrawGrid;
import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.integration.Integration;
import org.retropipes.dungeondiver7.Accelerators;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.ImageConstants;
import org.retropipes.dungeondiver7.creature.characterfiles.CharacterRegistration;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.current.GenerateDungeonTask;
import org.retropipes.dungeondiver7.dungeon.objects.Darkness;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.dungeon.objects.Player;
import org.retropipes.dungeondiver7.dungeon.objects.Wall;
import org.retropipes.dungeondiver7.game.replay.ReplayManager;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;
import org.retropipes.dungeondiver7.loader.music.MusicLoader;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Difficulty;
import org.retropipes.dungeondiver7.locale.GameString;
import org.retropipes.dungeondiver7.locale.Menu;
import org.retropipes.dungeondiver7.locale.Music;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.TimeTravel;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.manager.dungeon.DungeonManager;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.PartyInventory;
import org.retropipes.dungeondiver7.utility.RCLGenerator;
import org.retropipes.dungeondiver7.utility.ShotTypes;

class GameGUI {
    private class DifficultyEventHandler implements ActionListener, WindowListener {
	public DifficultyEventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final var cmd = e.getActionCommand();
	    final var gm = GameGUI.this;
	    if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
		gm.difficultyDialogOKButtonClicked();
	    } else {
		gm.difficultyDialogCancelButtonClicked();
	    }
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowClosing(final WindowEvent e) {
	    GameGUI.this.difficultyDialogCancelButtonClicked();
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	    // Ignore
	}
    }

    private class EventHandler implements KeyListener, WindowListener, MouseListener {
	EventHandler() {
	    // Do nothing
	}

	public void handleBlueLasers() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ShotTypes.BLUE);
		final var px = gm.getPlayerManager().getPlayerLocationX();
		final var py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleBombs() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_BOMBS) && PartyInventory.getBombsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_BOMBS)) {
		    PartyInventory.fireBomb();
		    gm.fireRange();
		} else {
		    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_BOMBS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleBoosts(final KeyEvent e) {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_BOOSTS) && PartyInventory.getBoostsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_BOOSTS)) {
		    PartyInventory.fireBoost();
		    final var keyCode = e.getKeyCode();
		    switch (keyCode) {
		    case KeyEvent.VK_LEFT:
			gm.updatePositionRelative(-2, 0);
			break;
		    case KeyEvent.VK_DOWN:
			gm.updatePositionRelative(0, 2);
			break;
		    case KeyEvent.VK_RIGHT:
			gm.updatePositionRelative(2, 0);
			break;
		    case KeyEvent.VK_UP:
			gm.updatePositionRelative(0, -2);
			break;
		    default:
			break;
		    }
		} else {
		    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_BOOSTS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleDisruptors() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ShotTypes.DISRUPTOR);
		final var px = gm.getPlayerManager().getPlayerLocationX();
		final var py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleHeatBombs() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_HEAT_BOMBS) && PartyInventory.getHeatBombsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_HEAT_BOMBS)) {
		    PartyInventory.fireHeatBomb();
		    gm.fireRange();
		} else {
		    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_HEAT_BOMBS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleIceBombs() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_ICE_BOMBS) && PartyInventory.getIceBombsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_ICE_BOMBS)) {
		    PartyInventory.fireIceBomb();
		    gm.fireRange();
		} else {
		    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_ICE_BOMBS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	private void handleKeystrokes(final KeyEvent e) {
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    switch (gm.otherAmmoMode) {
		    case GameLogic.OTHER_AMMO_MODE_MISSILES:
			this.handleMissiles();
			break;
		    case GameLogic.OTHER_AMMO_MODE_STUNNERS:
			this.handleStunners();
			break;
		    case GameLogic.OTHER_AMMO_MODE_BLUE_LASERS:
			this.handleBlueLasers();
			break;
		    case GameLogic.OTHER_AMMO_MODE_DISRUPTORS:
			this.handleDisruptors();
			break;
		    default:
			break;
		    }
		} else {
		    this.handleLasers();
		}
	    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    switch (gm.otherRangeMode) {
		    case GameLogic.OTHER_RANGE_MODE_BOMBS:
			this.handleBombs();
			break;
		    case GameLogic.OTHER_RANGE_MODE_HEAT_BOMBS:
			this.handleHeatBombs();
			break;
		    case GameLogic.OTHER_RANGE_MODE_ICE_BOMBS:
			this.handleIceBombs();
			break;
		    default:
			break;
		    }
		}
	    } else {
		final var currDir = gm.player.getDirection();
		final var newDir = this.mapKeyToDirection(e);
		if (currDir != newDir) {
		    this.handleTurns(newDir);
		} else if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    if (gm.otherToolMode == GameLogic.OTHER_TOOL_MODE_BOOSTS) {
			this.handleBoosts(e);
		    } else if (gm.otherToolMode == GameLogic.OTHER_TOOL_MODE_MAGNETS) {
			this.handleMagnets(e);
		    }
		} else {
		    this.handleMovement(e);
		}
	    }
	}

	public void handleLasers() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ShotTypes.GREEN);
		final var px = gm.getPlayerManager().getPlayerLocationX();
		final var py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleMagnets(final KeyEvent e) {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_MAGNETS) && PartyInventory.getMagnetsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_MAGNETS)) {
		    PartyInventory.fireMagnet();
		    final var keyCode = e.getKeyCode();
		    switch (keyCode) {
		    case KeyEvent.VK_LEFT:
			gm.updatePositionRelative(-3, 0);
			break;
		    case KeyEvent.VK_DOWN:
			gm.updatePositionRelative(0, 3);
			break;
		    case KeyEvent.VK_RIGHT:
			gm.updatePositionRelative(3, 0);
			break;
		    case KeyEvent.VK_UP:
			gm.updatePositionRelative(0, -3);
			break;
		    default:
			break;
		    }
		} else {
		    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_MAGNETS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleMissiles() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ShotTypes.MISSILE);
		final var px = gm.getPlayerManager().getPlayerLocationX();
		final var py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleMovement(final KeyEvent e) {
	    try {
		final var glm = DungeonDiver7.getStuffBag().getGameLogic();
		final var keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(-1, -1);
		    } else {
			glm.updatePositionRelative(-1, 0);
		    }
		    break;
		case KeyEvent.VK_DOWN:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(-1, 1);
		    } else {
			glm.updatePositionRelative(0, 1);
		    }
		    break;
		case KeyEvent.VK_RIGHT:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(1, 1);
		    } else {
			glm.updatePositionRelative(1, 0);
		    }
		    break;
		case KeyEvent.VK_UP:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(1, -1);
		    } else {
			glm.updatePositionRelative(0, -1);
		    }
		    break;
		case KeyEvent.VK_ENTER:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(0, 0);
		    }
		    break;
		case KeyEvent.VK_SPACE:
		    final var app = DungeonDiver7.getStuffBag();
		    final var m = app.getDungeonManager().getDungeon();
		    final var px = m.getPlayerLocationX(0);
		    final var py = m.getPlayerLocationY(0);
		    AbstractDungeonObject there = new Empty();
		    try {
			there = m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
		    } catch (final ArrayIndexOutOfBoundsException ae) {
			// Ignore
		    }
		    there.interactAction();
		    break;
		case KeyEvent.VK_NUMPAD7:
		case KeyEvent.VK_Q:
		    glm.updatePositionRelative(-1, -1);
		    break;
		case KeyEvent.VK_NUMPAD8:
		case KeyEvent.VK_W:
		    glm.updatePositionRelative(0, -1);
		    break;
		case KeyEvent.VK_NUMPAD9:
		case KeyEvent.VK_E:
		    glm.updatePositionRelative(1, -1);
		    break;
		case KeyEvent.VK_NUMPAD4:
		case KeyEvent.VK_A:
		    glm.updatePositionRelative(-1, 0);
		    break;
		case KeyEvent.VK_NUMPAD5:
		case KeyEvent.VK_S:
		    glm.updatePositionRelative(0, 0);
		    break;
		case KeyEvent.VK_NUMPAD6:
		case KeyEvent.VK_D:
		    glm.updatePositionRelative(1, 0);
		    break;
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_Z:
		    glm.updatePositionRelative(-1, 1);
		    break;
		case KeyEvent.VK_NUMPAD2:
		case KeyEvent.VK_X:
		    glm.updatePositionRelative(0, 1);
		    break;
		case KeyEvent.VK_NUMPAD3:
		case KeyEvent.VK_C:
		    glm.updatePositionRelative(1, 1);
		    break;
		default:
		    break;
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleStunners() {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ShotTypes.STUNNER);
		final var px = gm.getPlayerManager().getPlayerLocationX();
		final var py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleTurns(final Direction dir) {
	    try {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		var fired = false;
		switch (dir) {
		case WEST:
		    gm.player.setDirection(Direction.WEST);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, -1, 0);
		    }
		    fired = true;
		    break;
		case SOUTH:
		    gm.player.setDirection(Direction.SOUTH);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, 0, 1);
		    }
		    fired = true;
		    break;
		case EAST:
		    gm.player.setDirection(Direction.EAST);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, 1, 0);
		    }
		    fired = true;
		    break;
		case NORTH:
		    gm.player.setDirection(Direction.NORTH);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, 0, -1);
		    }
		    fired = true;
		    break;
		default:
		    break;
		}
		if (fired) {
		    gm.markPlayerAsDirty();
		    gm.redrawDungeon();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	    if (GameGUI.this.eventFlag && !Prefs.oneMove()) {
		this.handleKeystrokes(e);
	    }
	}

	@Override
	public void keyReleased(final KeyEvent e) {
	    if (GameGUI.this.eventFlag && Prefs.oneMove()) {
		this.handleKeystrokes(e);
	    }
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	    // Do nothing
	}

	public Direction mapKeyToDirection(final KeyEvent e) {
	    final var keyCode = e.getKeyCode();
	    return switch (keyCode) {
	    case KeyEvent.VK_LEFT -> Direction.WEST;
	    case KeyEvent.VK_DOWN -> Direction.SOUTH;
	    case KeyEvent.VK_RIGHT -> Direction.EAST;
	    case KeyEvent.VK_UP -> Direction.NORTH;
	    default -> Direction.NONE;
	    };
	}

	public Direction mapMouseToDirection(final MouseEvent me) {
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    final var x = me.getX();
	    final var y = me.getY();
	    final var px = gm.getPlayerManager().getPlayerLocationX();
	    final var py = gm.getPlayerManager().getPlayerLocationY();
	    final var destX = (int) Math.signum(x / ImageConstants.SIZE - px);
	    final var destY = (int) Math.signum(y / ImageConstants.SIZE - py);
	    return DirectionResolver.resolve(destX, destY);
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final var game = DungeonDiver7.getStuffBag().getGameLogic();
		if (e.isShiftDown()) {
		    final var x = e.getX();
		    final var y = e.getY();
		    game.identifyObject(x, y);
		} else if (e.getButton() == MouseEvent.BUTTON1) {
		    // Move
		    final var dir = this.mapMouseToDirection(e);
		    final var tankDir = game.player.getDirection();
		    if (tankDir != dir) {
			this.handleTurns(dir);
		    } else {
			final var x = e.getX();
			final var y = e.getY();
			final var px = game.getPlayerManager().getPlayerLocationX();
			final var py = game.getPlayerManager().getPlayerLocationY();
			final var destX = (int) Math.signum(x / ImageConstants.SIZE - px);
			final var destY = (int) Math.signum(y / ImageConstants.SIZE - py);
			game.updatePositionRelative(destX, destY);
		    }
		} else if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
		    // Fire Laser
		    game.setLaserType(ShotTypes.GREEN);
		    final var px = game.getPlayerManager().getPlayerLocationX();
		    final var py = game.getPlayerManager().getPlayerLocationY();
		    game.fireLaser(px, py, game.player);
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	    // Do nothing
	}

	// handle mouse
	@Override
	public void mousePressed(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	    // Do nothing
	}

	// Handle windows
	@Override
	public void windowActivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent we) {
	    try {
		final var app = DungeonDiver7.getStuffBag();
		var success = false;
		var status = 0;
		if (app.getDungeonManager().getDirty()) {
		    app.getDungeonManager();
		    status = DungeonManager.showSaveDialog();
		    if (status == CommonDialogs.YES_OPTION) {
			app.getDungeonManager();
			success = DungeonManager.saveGame();
			if (success) {
			    app.getGameLogic().exitGame();
			}
		    } else if (status == CommonDialogs.NO_OPTION) {
			app.getGameLogic().exitGame();
		    }
		} else {
		    app.getGameLogic().exitGame();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void windowDeactivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent we) {
	    // Do nothing
	}
    }

    private class MenuHandler implements ActionListener {
	public MenuHandler() {
	    // Do nothing
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final var app = DungeonDiver7.getStuffBag();
		final var cmd = e.getActionCommand();
		final var game = app.getGameLogic();
		final var gui = GameGUI.this;
		if (cmd.equals(Strings.menu(Menu.RESET_CURRENT_LEVEL))) {
		    final var result = CommonDialogs.showConfirmDialog(Strings.menu(Menu.CONFIRM_RESET_CURRENT_LEVEL),
			    Strings.untranslated(Untranslated.PROGRAM_NAME));
		    if (result == CommonDialogs.YES_OPTION) {
			game.abortAndWaitForMLOLoop();
			game.resetCurrentLevel();
		    }
		} else if (cmd.equals(Strings.menu(Menu.SHOW_SCORE_TABLE))) {
		    game.showScoreTable();
		} else if (cmd.equals(Strings.menu(Menu.REPLAY_SOLUTION))) {
		    game.abortAndWaitForMLOLoop();
		    game.replaySolution();
		} else if (cmd.equals(Strings.menu(Menu.RECORD_SOLUTION))) {
		    game.toggleRecording();
		} else if (cmd.equals(Strings.menu(Menu.LOAD_REPLAY_FILE))) {
		    game.abortAndWaitForMLOLoop();
		    ReplayManager.loadReplay();
		} else if (cmd.equals(Strings.menu(Menu.PREVIOUS_LEVEL))) {
		    game.abortAndWaitForMLOLoop();
		    game.previousLevel();
		} else if (cmd.equals(Strings.menu(Menu.SKIP_LEVEL))) {
		    game.abortAndWaitForMLOLoop();
		    game.solvedLevel(false);
		} else if (cmd.equals(Strings.menu(Menu.LOAD_LEVEL))) {
		    game.abortAndWaitForMLOLoop();
		    game.loadLevel();
		} else if (cmd.equals(Strings.menu(Menu.SHOW_HINT))) {
		    CommonDialogs.showDialog(app.getDungeonManager().getDungeon().getHint().trim());
		} else if (cmd.equals(Strings.menu(Menu.CHEATS))) {
		    game.enterCheatCode();
		} else if (cmd.equals(Strings.menu(Menu.CHANGE_OTHER_AMMO))) {
		    game.changeOtherAmmoMode();
		} else if (cmd.equals(Strings.menu(Menu.CHANGE_OTHER_TOOL))) {
		    game.changeOtherToolMode();
		} else if (cmd.equals(Strings.menu(Menu.CHANGE_OTHER_RANGE))) {
		    game.changeOtherRangeMode();
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_PAST))) {
		    // Time Travel: Distant Past
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_PAST);
		    gui.gameEraDistantPast.setSelected(true);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.PAST))) {
		    // Time Travel: Past
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PAST);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(true);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.PRESENT))) {
		    // Time Travel: Present
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PRESENT);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(true);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.FUTURE))) {
		    // Time Travel: Future
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_FUTURE);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(true);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_FUTURE))) {
		    // Time Travel: Distant Future
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_FUTURE);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(true);
		} else if (cmd.equals(Strings.menu(Menu.NEW_GAME))) {
		    // Start a new game
		    final var proceed = app.getGameLogic().newGame();
		    if (proceed) {
			new GenerateDungeonTask(true).start();
		    }
		} else if (cmd.equals(Strings.menu(Menu.REGISTER_CHARACTER))) {
		    // Register Character
		    CharacterRegistration.registerCharacter();
		} else if (cmd.equals(Strings.menu(Menu.UNREGISTER_CHARACTER))) {
		    // Unregister Character
		    CharacterRegistration.unregisterCharacter();
		} else if (cmd.equals(Strings.menu(Menu.REMOVE_CHARACTER))) {
		    // Confirm
		    final var confirm = CommonDialogs
			    .showConfirmDialog("WARNING: This will DELETE the character from disk,\n"
				    + "and CANNOT be undone! Proceed anyway?", "Remove Character");
		    if (confirm == CommonDialogs.YES_OPTION) {
			// Remove Character
			CharacterRegistration.removeCharacter();
		    }
		} else if (cmd.equals(Strings.menu(Menu.SHOW_EQUIPMENT))) {
		    InventoryViewer.showEquipmentDialog();
		} else if (cmd.equals(Strings.menu(Menu.VIEW_STATISTICS))) {
		    // View Statistics
		    StatisticsViewer.viewStatistics();
		}
		app.getMenuManager().checkFlags();
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    private static Darkness DARK = new Darkness();

    private static void checkMenus() {
	final var edit = DungeonDiver7.getStuffBag().getEditor();
	final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	if (a.tryUndo()) {
	    edit.enableUndo();
	} else {
	    edit.disableUndo();
	}
	if (a.tryRedo()) {
	    edit.enableRedo();
	} else {
	    edit.disableRedo();
	}
    }

    static int[] getEnabledDifficulties() {
	final var temp = new ArrayList<Integer>();
	if (Prefs.isKidsDifficultyEnabled()) {
	    temp.add(Integer.valueOf(Difficulty.KIDS.ordinal()));
	}
	if (Prefs.isEasyDifficultyEnabled()) {
	    temp.add(Integer.valueOf(Difficulty.EASY.ordinal()));
	}
	if (Prefs.isMediumDifficultyEnabled()) {
	    temp.add(Integer.valueOf(Difficulty.MEDIUM.ordinal()));
	}
	if (Prefs.isHardDifficultyEnabled()) {
	    temp.add(Integer.valueOf(Difficulty.HARD.ordinal()));
	}
	if (Prefs.isDeadlyDifficultyEnabled()) {
	    temp.add(Integer.valueOf(Difficulty.DEADLY.ordinal()));
	}
	final var temp2 = temp.toArray(new Integer[temp.size()]);
	final var retVal = new int[temp2.length];
	for (var x = 0; x < temp2.length; x++) {
	    retVal[x] = temp2[x];
	}
	return retVal;
    }

    // Fields
    private MainWindow mainWindow;
    private EventHandler handler;
    private MainContent borderPane, scorePane, infoPane, outerOutputPane, difficultyPane;
    private JLabel messageLabel;
    private JLabel scoreMoves;
    private JLabel scoreShots;
    private JLabel scoreOthers;
    private JLabel otherAmmoLeft;
    private JLabel otherToolsLeft;
    private JLabel otherRangesLeft;
    private JLabel levelInfo;
    private JButton difficultyOK;
    private DifficultyEventHandler dhandler;
    private GameViewingWindowManager vwMgr = null;
    private final StatGUI sg;
    private DrawGrid drawGrid;
    private GameDraw outputPane;
    private boolean knm;
    private boolean deferredRedraw;
    boolean eventFlag;
    private boolean newGameResult;
    private JList<String> difficultyList;
    private JMenu gameTimeTravelSubMenu;
    JCheckBoxMenuItem gameEraDistantPast, gameEraPast, gameEraPresent, gameEraFuture, gameEraDistantFuture;
    private JMenuItem gameReset, gameShowTable, gameReplaySolution, gameLoadLPB, gamePreviousLevel, gameSkipLevel,
	    gameLoadLevel, gameShowHint, gameCheats, gameChangeOtherAmmoMode, gameChangeOtherToolMode,
	    gameChangeOtherRangeMode, gameNewGame, gameEquipment, gameRegisterCharacter, gameUnregisterCharacter,
	    gameRemoveCharacter, gameViewStats;
    private JCheckBoxMenuItem gameRecordSolution;

    // Constructors
    public GameGUI() {
	this.deferredRedraw = false;
	this.eventFlag = true;
	this.newGameResult = false;
	this.sg = new StatGUI();
    }

    public void activeLanguageChanged() {
	this.setUpDifficultyDialog();
    }

    public void attachAccelerators(final Accelerators accel) {
	this.gameReset.setAccelerator(accel.gameResetAccel);
	this.gameShowTable.setAccelerator(accel.gameShowTableAccel);
    }

    public void attachMenus() {
	final var app = DungeonDiver7.getStuffBag();
	Integration.integrate().setDefaultMenuBar(app.getMenuManager().getMainMenuBar());
	app.getMenuManager().checkFlags();
    }

    public JMenu createCommandsMenu() {
	final var mhandler = new MenuHandler();
	final var gameMenu = new JMenu(Strings.menu(Menu.GAME));
	this.gameTimeTravelSubMenu = new JMenu(Strings.menu(Menu.TIME_TRAVEL));
	this.gameNewGame = new JMenuItem(Strings.menu(Menu.NEW_GAME));
	this.gameReset = new JMenuItem(Strings.menu(Menu.RESET_CURRENT_LEVEL));
	this.gameShowTable = new JMenuItem(Strings.menu(Menu.SHOW_SCORE_TABLE));
	this.gameReplaySolution = new JMenuItem(Strings.menu(Menu.REPLAY_SOLUTION));
	this.gameRecordSolution = new JCheckBoxMenuItem(Strings.menu(Menu.RECORD_SOLUTION));
	this.gameLoadLPB = new JMenuItem(Strings.menu(Menu.LOAD_REPLAY_FILE));
	this.gamePreviousLevel = new JMenuItem(Strings.menu(Menu.PREVIOUS_LEVEL));
	this.gameSkipLevel = new JMenuItem(Strings.menu(Menu.SKIP_LEVEL));
	this.gameLoadLevel = new JMenuItem(Strings.menu(Menu.LOAD_LEVEL));
	this.gameShowHint = new JMenuItem(Strings.menu(Menu.SHOW_HINT));
	this.gameCheats = new JMenuItem(Strings.menu(Menu.CHEATS));
	this.gameChangeOtherAmmoMode = new JMenuItem(Strings.menu(Menu.CHANGE_OTHER_AMMO));
	this.gameChangeOtherToolMode = new JMenuItem(Strings.menu(Menu.CHANGE_OTHER_TOOL));
	this.gameChangeOtherRangeMode = new JMenuItem(Strings.menu(Menu.CHANGE_OTHER_RANGE));
	this.gameEraDistantPast = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.FAR_PAST), false);
	this.gameEraPast = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.PAST), false);
	this.gameEraPresent = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.PRESENT), true);
	this.gameEraFuture = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.FUTURE), false);
	this.gameEraDistantFuture = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.FAR_FUTURE), false);
	this.gameRegisterCharacter = new JMenuItem(Strings.menu(Menu.REGISTER_CHARACTER));
	this.gameUnregisterCharacter = new JMenuItem(Strings.menu(Menu.UNREGISTER_CHARACTER));
	this.gameRemoveCharacter = new JMenuItem(Strings.menu(Menu.REMOVE_CHARACTER));
	this.gameEquipment = new JMenuItem(Strings.menu(Menu.SHOW_EQUIPMENT));
	this.gameViewStats = new JMenuItem(Strings.menu(Menu.VIEW_STATISTICS));
	this.gameNewGame.addActionListener(mhandler);
	this.gameReset.addActionListener(mhandler);
	this.gameShowTable.addActionListener(mhandler);
	this.gameReplaySolution.addActionListener(mhandler);
	this.gameRecordSolution.addActionListener(mhandler);
	this.gameLoadLPB.addActionListener(mhandler);
	this.gamePreviousLevel.addActionListener(mhandler);
	this.gameSkipLevel.addActionListener(mhandler);
	this.gameLoadLevel.addActionListener(mhandler);
	this.gameShowHint.addActionListener(mhandler);
	this.gameCheats.addActionListener(mhandler);
	this.gameChangeOtherAmmoMode.addActionListener(mhandler);
	this.gameChangeOtherToolMode.addActionListener(mhandler);
	this.gameChangeOtherRangeMode.addActionListener(mhandler);
	this.gameEraDistantPast.addActionListener(mhandler);
	this.gameEraPast.addActionListener(mhandler);
	this.gameEraPresent.addActionListener(mhandler);
	this.gameEraFuture.addActionListener(mhandler);
	this.gameEraDistantFuture.addActionListener(mhandler);
	this.gameRegisterCharacter.addActionListener(mhandler);
	this.gameUnregisterCharacter.addActionListener(mhandler);
	this.gameRemoveCharacter.addActionListener(mhandler);
	this.gameEquipment.addActionListener(mhandler);
	this.gameViewStats.addActionListener(mhandler);
	this.gameTimeTravelSubMenu.add(this.gameEraDistantPast);
	this.gameTimeTravelSubMenu.add(this.gameEraPast);
	this.gameTimeTravelSubMenu.add(this.gameEraPresent);
	this.gameTimeTravelSubMenu.add(this.gameEraFuture);
	this.gameTimeTravelSubMenu.add(this.gameEraDistantFuture);
	gameMenu.add(this.gameNewGame);
	gameMenu.add(this.gameReset);
	gameMenu.add(this.gameShowTable);
	gameMenu.add(this.gameReplaySolution);
	gameMenu.add(this.gameRecordSolution);
	gameMenu.add(this.gameLoadLPB);
	gameMenu.add(this.gamePreviousLevel);
	gameMenu.add(this.gameSkipLevel);
	gameMenu.add(this.gameLoadLevel);
	gameMenu.add(this.gameShowHint);
	gameMenu.add(this.gameCheats);
	gameMenu.add(this.gameChangeOtherAmmoMode);
	gameMenu.add(this.gameChangeOtherToolMode);
	gameMenu.add(this.gameChangeOtherRangeMode);
	gameMenu.add(this.gameTimeTravelSubMenu);
	gameMenu.add(this.gameEquipment);
	gameMenu.add(this.gameRegisterCharacter);
	gameMenu.add(this.gameUnregisterCharacter);
	gameMenu.add(this.gameRemoveCharacter);
	gameMenu.add(this.gameViewStats);
	return gameMenu;
    }

    void difficultyDialogCancelButtonClicked() {
	this.hideDifficultyDialog();
	this.newGameResult = false;
    }

    void difficultyDialogOKButtonClicked() {
	this.hideDifficultyDialog();
	if (this.difficultyList.isSelectedIndex(Difficulty.KIDS.ordinal())) {
	    Prefs.setKidsDifficultyEnabled(true);
	} else {
	    Prefs.setKidsDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.EASY.ordinal())) {
	    Prefs.setEasyDifficultyEnabled(true);
	} else {
	    Prefs.setEasyDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.MEDIUM.ordinal())) {
	    Prefs.setMediumDifficultyEnabled(true);
	} else {
	    Prefs.setMediumDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.HARD.ordinal())) {
	    Prefs.setHardDifficultyEnabled(true);
	} else {
	    Prefs.setHardDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(Difficulty.DEADLY.ordinal())) {
	    Prefs.setDeadlyDifficultyEnabled(true);
	} else {
	    Prefs.setDeadlyDifficultyEnabled(false);
	}
	this.newGameResult = true;
    }

    public void disableDirtyCommands() {
	// Do nothing
    }

    public void disableEvents() {
	this.mainWindow.setEnabled(false);
	this.eventFlag = false;
    }

    public void disableLoadedCommands() {
	// Do nothing
    }

    public void disableModeCommands() {
	this.gameNewGame.setEnabled(true);
	this.gameReset.setEnabled(false);
	this.gameShowTable.setEnabled(false);
	this.gameReplaySolution.setEnabled(false);
	this.gameRecordSolution.setEnabled(false);
	this.gameLoadLPB.setEnabled(false);
	this.gamePreviousLevel.setEnabled(false);
	this.gameSkipLevel.setEnabled(false);
	this.gameLoadLevel.setEnabled(false);
	this.gameShowHint.setEnabled(false);
	this.gameCheats.setEnabled(false);
	this.gameChangeOtherAmmoMode.setEnabled(false);
	this.gameChangeOtherToolMode.setEnabled(false);
	this.gameChangeOtherRangeMode.setEnabled(false);
	this.gameEraDistantPast.setEnabled(false);
	this.gameEraPast.setEnabled(false);
	this.gameEraPresent.setEnabled(false);
	this.gameEraFuture.setEnabled(false);
	this.gameEraDistantFuture.setEnabled(false);
	this.gameRegisterCharacter.setEnabled(true);
	this.gameUnregisterCharacter.setEnabled(true);
	this.gameRemoveCharacter.setEnabled(true);
	this.gameEquipment.setEnabled(false);
	this.gameViewStats.setEnabled(false);
    }

    void disableRecording() {
	this.gameRecordSolution.setSelected(false);
    }

    public void enableDirtyCommands() {
	// Do nothing
    }

    public void enableEvents() {
	this.mainWindow.setEnabled(true);
	this.eventFlag = true;
    }

    public void enableLoadedCommands() {
	// Do nothing
    }

    public void enableModeCommands() {
	this.gameNewGame.setEnabled(false);
	this.gameReset.setEnabled(true);
	this.gameShowTable.setEnabled(true);
	this.gameReplaySolution.setEnabled(true);
	this.gameRecordSolution.setEnabled(true);
	this.gameLoadLPB.setEnabled(true);
	this.gamePreviousLevel.setEnabled(true);
	this.gameSkipLevel.setEnabled(true);
	this.gameLoadLevel.setEnabled(true);
	this.gameShowHint.setEnabled(true);
	this.gameCheats.setEnabled(true);
	this.gameChangeOtherAmmoMode.setEnabled(true);
	this.gameChangeOtherToolMode.setEnabled(true);
	this.gameChangeOtherRangeMode.setEnabled(true);
	this.gameEraDistantPast.setEnabled(true);
	this.gameEraPast.setEnabled(true);
	this.gameEraPresent.setEnabled(true);
	this.gameEraFuture.setEnabled(true);
	this.gameEraDistantFuture.setEnabled(true);
	this.gameRegisterCharacter.setEnabled(false);
	this.gameUnregisterCharacter.setEnabled(false);
	this.gameRemoveCharacter.setEnabled(false);
	this.gameEquipment.setEnabled(true);
	this.gameViewStats.setEnabled(true);
    }

    public void hideOutput() {
	if (this.mainWindow != null) {
	    this.mainWindow.removeKeyListener(this.handler);
	    this.mainWindow.removeWindowListener(this.handler);
	    this.outputPane.removeMouseListener(this.handler);
	    this.mainWindow.restoreSaved();
	}
    }

    void initViewManager() {
	if (this.vwMgr == null) {
	    this.vwMgr = DungeonDiver7.getStuffBag().getGameLogic().getViewManager();
	    this.setUpGUI();
	}
    }

    public void keepNextMessage() {
	this.knm = true;
    }

    boolean newGame() {
	DungeonDiver7.getStuffBag().getObjects().enableAllObjects();
	this.difficultyList.clearSelection();
	final var retVal = GameGUI.getEnabledDifficulties();
	this.difficultyList.setSelectedIndices(retVal);
	this.showDifficultyDialog();
	return this.newGameResult;
    }

    public void redrawDungeon() {
	// Draw the maze
	final var app = DungeonDiver7.getStuffBag();
	final var m = app.getDungeonManager().getDungeon();
	int x, y, u, v;
	int xFix, yFix;
	boolean visible;
	u = m.getPlayerLocationX(0);
	v = m.getPlayerLocationY(0);
	final AbstractDungeonObject wall = new Wall();
	for (x = this.vwMgr.getViewingWindowLocationX(); x <= this.vwMgr.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = this.vwMgr.getViewingWindowLocationY(); y <= this.vwMgr
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - this.vwMgr.getViewingWindowLocationX();
		yFix = y - this.vwMgr.getViewingWindowLocationY();
		visible = app.getDungeonManager().getDungeon().isSquareVisible(u, v, y, x, 0);
		try {
		    if (visible) {
			final var obj1 = m.getCell(y, x, 0, DungeonConstants.LAYER_LOWER_GROUND);
			final var obj2 = m.getCell(y, x, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
			final var img1 = obj1.gameRenderHook(y, x);
			final var img2 = obj2.gameRenderHook(y, x);
			if (u == y && v == x) {
			    final AbstractDungeonObject obj3 = new Player();
			    final var img3 = obj3.gameRenderHook(y, x);
			    final var cacheName = Strings.compositeCacheName(obj1.getCacheName(), obj2.getCacheName(),
				    obj3.getCacheName());
			    final var img = ImageCompositor.composite(cacheName, img1, img2, img3);
			    this.drawGrid.setImageCell(img, xFix, yFix);
			} else {
			    final var cacheName = Strings.compositeCacheName(obj1.getCacheName(), obj2.getCacheName());
			    final var img = ImageCompositor.composite(cacheName, img1, img2);
			    this.drawGrid.setImageCell(img, xFix, yFix);
			}
		    } else {
			this.drawGrid.setImageCell(
				ObjectImageLoader.load(GameGUI.DARK.getCacheName(), GameGUI.DARK.getBaseID()), xFix,
				yFix);
		    }
		} catch (final ArrayIndexOutOfBoundsException ae) {
		    this.drawGrid.setImageCell(wall.gameRenderHook(y, x), xFix, yFix);
		}
	    }
	}
	if (this.knm) {
	    this.knm = false;
	} else {
	    this.setStatusMessage(" ");
	}
	this.outputPane.repaint();
	this.showOutputAndKeepMusic();
    }

    private void resetBorderPane() {
	this.borderPane.removeAll();
	this.borderPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
	this.borderPane.add(this.sg.getStatsPane(), BorderLayout.EAST);
    }

    public void setInitialState() {
	this.disableModeCommands();
    }

    public void setStatusMessage(final String msg) {
	this.messageLabel.setText(msg);
    }

    private void setUpDifficultyDialog() {
	// Set up Difficulty Dialog
	this.dhandler = new DifficultyEventHandler();
	this.difficultyPane = this.mainWindow.createContent();
	final var listPane = this.mainWindow.createContent();
	final var buttonPane = this.mainWindow.createContent();
	this.difficultyList = new JList<>(Strings.allDifficulties());
	this.difficultyOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
	final var cancelButton = new JButton(Strings.dialog(DialogString.CANCEL_BUTTON));
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(this.difficultyOK);
	buttonPane.add(cancelButton);
	listPane.setLayout(new FlowLayout());
	listPane.add(this.difficultyList);
	this.difficultyPane.setLayout(new BorderLayout());
	this.difficultyPane.add(listPane, BorderLayout.CENTER);
	this.difficultyPane.add(buttonPane, BorderLayout.SOUTH);
	this.difficultyOK.setDefaultCapable(true);
	cancelButton.setDefaultCapable(false);
	this.difficultyOK.addActionListener(this.dhandler);
	cancelButton.addActionListener(this.dhandler);
    }

    private void showDifficultyDialog() {
	this.mainWindow.setAndSave(this.difficultyPane, Strings.game(GameString.SELECT_DIFFICULTY), this.difficultyOK);
	this.mainWindow.pack();
	this.mainWindow.addWindowListener(this.dhandler);
    }

    private void hideDifficultyDialog() {
	this.mainWindow.removeWindowListener(this.dhandler);
	this.mainWindow.restoreSaved();
	this.mainWindow.pack();
    }

    private void setUpGUI() {
	this.handler = new EventHandler();
	this.mainWindow = MainWindow.mainWindow();
	this.borderPane = this.mainWindow.createContent();
	this.borderPane.setLayout(new BorderLayout());
	this.messageLabel = new JLabel(" ");
	this.messageLabel.setOpaque(true);
	this.drawGrid = new DrawGrid(Prefs.getViewingWindowSize());
	this.outputPane = new GameDraw(this.drawGrid);
	// Pasted code
	this.borderPane = this.mainWindow.createContent();
	this.borderPane.setLayout(new BorderLayout());
	this.mainWindow = MainWindow.mainWindow();
	this.outerOutputPane = RCLGenerator.generateRowColumnLabels();
	this.outputPane = new GameDraw();
	this.outputPane.setLayout(new GridLayout(GameViewingWindowManager.getFixedViewingWindowSizeX(),
		GameViewingWindowManager.getFixedViewingWindowSizeY()));
	this.scoreMoves = new JLabel(LocaleUtils.subst(Strings.game(GameString.MOVES), Integer.toString(0)));
	this.scoreShots = new JLabel(LocaleUtils.subst(Strings.game(GameString.SHOTS), Integer.toString(0)));
	this.scoreOthers = new JLabel(LocaleUtils.subst(Strings.game(GameString.OTHERS), Integer.toString(0)));
	this.otherAmmoLeft = new JLabel(LocaleUtils.subst(Strings.game(GameString.MISSILES), Integer.toString(0)));
	this.otherToolsLeft = new JLabel(LocaleUtils.subst(Strings.game(GameString.BOOSTS), Integer.toString(0)));
	this.otherRangesLeft = new JLabel(LocaleUtils.subst(Strings.game(GameString.BOMBS), Integer.toString(0)));
	this.scorePane = this.mainWindow.createContent();
	this.scorePane.setLayout(new FlowLayout());
	this.scorePane.add(this.scoreMoves);
	this.scorePane.add(this.scoreShots);
	this.scorePane.add(this.scoreOthers);
	this.scorePane.add(this.otherAmmoLeft);
	this.scorePane.add(this.otherToolsLeft);
	this.scorePane.add(this.otherRangesLeft);
	this.levelInfo = new JLabel(Strings.SPACE);
	this.infoPane = this.mainWindow.createContent();
	this.infoPane.setLayout(new FlowLayout());
	this.infoPane.add(this.levelInfo);
	this.scoreMoves.setLabelFor(this.outputPane);
	this.scoreShots.setLabelFor(this.outputPane);
	this.scoreOthers.setLabelFor(this.outputPane);
	this.otherAmmoLeft.setLabelFor(this.outputPane);
	this.otherToolsLeft.setLabelFor(this.outputPane);
	this.otherRangesLeft.setLabelFor(this.outputPane);
	this.levelInfo.setLabelFor(this.outputPane);
	this.outerOutputPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(this.outerOutputPane, BorderLayout.CENTER);
	this.borderPane.add(this.scorePane, BorderLayout.NORTH);
	this.borderPane.add(this.infoPane, BorderLayout.SOUTH);
	this.setUpDifficultyDialog();
    }

    public void showOutput() {
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	final var zoneID = PartyManager.getParty().getZone();
	if (zoneID == AbstractDungeon.getMaxLevels() - 1) {
	    MusicLoader.playMusic(Music.LAIR);
	} else {
	    MusicLoader.playMusic(Music.DUNGEON);
	}
	this.showOutputCommon();
    }

    public void showOutputAndKeepMusic() {
	this.showOutputCommon();
    }

    private void showOutputCommon() {
	final var app = DungeonDiver7.getStuffBag();
	this.mainWindow.setAndSave(this.borderPane, Strings.untranslated(Untranslated.PROGRAM_NAME));
	this.mainWindow.addKeyListener(this.handler);
	this.mainWindow.addWindowListener(this.handler);
	this.outputPane.addMouseListener(this.handler);
	Integration.integrate().setDefaultMenuBar(app.getMenuManager().getMainMenuBar());
	if (this.deferredRedraw) {
	    this.deferredRedraw = false;
	    this.redrawDungeon();
	}
	app.getMenuManager().checkFlags();
	GameGUI.checkMenus();
	this.updateStats();
    }

    void updateGameGUI() {
	this.resetBorderPane();
	this.sg.updateImages();
	this.sg.updateStats();
    }

    public void updateStats() {
	this.sg.updateStats();
    }

    void viewingWindowSizeChanged() {
	this.setUpGUI();
	this.updateGameGUI();
	this.deferredRedraw = true;
    }
}
