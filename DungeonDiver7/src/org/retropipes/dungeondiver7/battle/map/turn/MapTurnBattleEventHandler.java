package org.retropipes.dungeondiver7.battle.map.turn;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.prefs.Prefs;

class MapTurnBattleEventHandler extends AbstractAction implements KeyListener {
    private final MapTurnBattleGUI gui;
    private static final long serialVersionUID = 20239525230523524L;

    public MapTurnBattleEventHandler(MapTurnBattleGUI mapTurnBattleGUI) {
	gui = mapTurnBattleGUI;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    if (e.getSource() instanceof JButton) {
		SoundLoader.playSound(Sounds.CLICK);
	    }
	    final var cmd = e.getActionCommand();
	    final var b = DungeonDiver7.getStuffBag().getBattle();
	    // Do Player Actions
	    if (cmd.equals("Cast Spell") || cmd.equals("c")) {
		// Cast Spell
		b.doPlayerActions(BattleAction.CAST_SPELL);
	    } else if (cmd.equals("Steal") || cmd.equals("t")) {
		// Steal Money
		b.doPlayerActions(BattleAction.STEAL);
	    } else if (cmd.equals("Drain") || cmd.equals("d")) {
		// Drain Enemy
		b.doPlayerActions(BattleAction.DRAIN);
	    } else if (cmd.equals("End Turn") || cmd.equals("e")) {
		// End Turn
		b.endTurn();
	    }
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }

    private void handleMovement(final KeyEvent e) {
	try {
	    if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
		if (e.isMetaDown()) {
		    return;
		}
	    } else if (e.isControlDown()) {
		return;
	    }
	    final var bl = DungeonDiver7.getStuffBag().getBattle();
	    final var bg = gui;
	    if (bg.eventHandlersOn) {
		final var keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_NUMPAD4:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
		    bl.updatePosition(-1, 0);
		    break;
		case KeyEvent.VK_NUMPAD2:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_X:
		    bl.updatePosition(0, 1);
		    break;
		case KeyEvent.VK_NUMPAD6:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
		    bl.updatePosition(1, 0);
		    break;
		case KeyEvent.VK_NUMPAD8:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
		    bl.updatePosition(0, -1);
		    break;
		case KeyEvent.VK_NUMPAD7:
		case KeyEvent.VK_Q:
		    bl.updatePosition(-1, -1);
		    break;
		case KeyEvent.VK_NUMPAD9:
		case KeyEvent.VK_E:
		    bl.updatePosition(1, -1);
		    break;
		case KeyEvent.VK_NUMPAD3:
		case KeyEvent.VK_C:
		    bl.updatePosition(1, 1);
		    break;
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_Z:
		    bl.updatePosition(-1, 1);
		    break;
		case KeyEvent.VK_NUMPAD5:
		case KeyEvent.VK_S:
		    // Confirm before attacking self
		    final var res = CommonDialogs.showConfirmDialog("Are you sure you want to attack yourself?",
			    "Battle");
		    if (res == CommonDialogs.YES_OPTION) {
			bl.updatePosition(0, 0);
		    }
		    break;
		default:
		    break;
		}
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }

    @Override
    public void keyPressed(final KeyEvent e) {
	if (!Prefs.oneMove()) {
	    this.handleMovement(e);
	}
    }

    @Override
    public void keyReleased(final KeyEvent e) {
	if (Prefs.oneMove()) {
	    this.handleMovement(e);
	}
    }

    @Override
    public void keyTyped(final KeyEvent e) {
	// Do nothing
    }
}