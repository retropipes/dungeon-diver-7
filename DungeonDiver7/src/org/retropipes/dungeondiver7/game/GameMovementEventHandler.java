package org.retropipes.dungeondiver7.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.ImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.locale.GameString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.PartyInventory;
import org.retropipes.dungeondiver7.utility.ShotTypes;

class GameMovementEventHandler implements KeyListener, MouseListener {
    /**
     * 
     */
    private final GameGUI gameGUI;

    GameMovementEventHandler(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
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
    	    DungeonObject there = new Empty();
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
        if (this.gameGUI.eventFlag && !Prefs.oneMove()) {
    	this.handleKeystrokes(e);
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        if (this.gameGUI.eventFlag && Prefs.oneMove()) {
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
}