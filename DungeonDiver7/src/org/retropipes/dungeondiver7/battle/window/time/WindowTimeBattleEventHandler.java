/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.window.time;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.battle.BattleResult;

class WindowTimeBattleEventHandler extends AbstractAction {
    /**
     * 
     */
    private final WindowTimeBattleGUI gui;
    private static final long serialVersionUID = 20239525230523523L;

    public WindowTimeBattleEventHandler(WindowTimeBattleGUI windowTimeBattleGUI) {
	this.gui = windowTimeBattleGUI;
	// Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    var success = true;
	    final var cmd = e.getActionCommand();
	    final var wbg = this.gui;
	    final var b = DungeonDiver7.getStuffBag().getBattle();
	    // Clear Message Area
	    wbg.clearMessageArea();
	    // Display Beginning Stats
	    wbg.setStatusMessage("*** Beginning of Round ***");
	    b.displayBattleStats();
	    wbg.setStatusMessage("*** Beginning of Round ***\n");
	    // Do Player Actions
	    if (cmd.equals("Attack") || cmd.equals("a")) {
		// Attack
		success = b.doPlayerActions(BattleAction.ATTACK);
	    } else if (cmd.equals("Flee") || cmd.equals("f")) {
		// Try to Flee
		success = b.doPlayerActions(BattleAction.FLEE);
		if (success) {
		    // Strip Extra Newline Character
		    wbg.stripExtraNewLine();
		    // Pack Battle Frame
		    wbg.battleFrame.pack();
		    // Get out of here
		    b.doResult();
		    return;
		}
		success = b.doPlayerActions(BattleAction.ATTACK);
	    } else if (cmd.equals("Continue")) {
		// Battle Done
		b.battleDone();
		return;
	    } else if (cmd.equals("Cast Spell") || cmd.equals("c")) {
		// Cast Spell
		success = b.doPlayerActions(BattleAction.CAST_SPELL);
		if (!success) {
		    // Strip Two Extra Newline Characters
		    wbg.stripExtraNewLine();
		    wbg.stripExtraNewLine();
		    // Pack Battle Frame
		    wbg.battleFrame.pack();
		    // Get out of here
		    return;
		}
	    } else if (cmd.equals("Steal") || cmd.equals("s")) {
		// Steal Money
		success = b.doPlayerActions(BattleAction.STEAL);
	    } else if (cmd.equals("Drain") || cmd.equals("d")) {
		// Drain Enemy
		success = b.doPlayerActions(BattleAction.DRAIN);
	    }
	    // Maintain Player Effects
	    b.maintainEffects(true);
	    // Check result
	    final var bResult = b.getResult();
	    if (bResult != BattleResult.IN_PROGRESS) {
		b.setResult(bResult);
		b.doResult();
	    }
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }
}