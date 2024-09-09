/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.map.time;

import java.util.TimerTask;

import org.retropipes.diane.Diane;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.battle.BattleResult;

class MapTimeBattleEnemyTask extends TimerTask {
    private final MapTimeBattleLogic logic;

    public MapTimeBattleEnemyTask(MapTimeBattleLogic mapTimeBattleLogic) {
	logic = mapTimeBattleLogic;
    }

    private void enemyAct() {
	final var gui = logic.battleGUI;
	// Do Enemy Actions
	logic.executeNextAIAction();
	gui.resetEnemyActionBar();
	// Maintain Enemy Effects
	logic.maintainEffects(false);
	// Display Active Effects
	logic.displayActiveEffects();
	// Display End Stats
	logic.displayBattleStats();
	// Check Result
	final var bResult = logic.getResult();
	if (bResult != BattleResult.IN_PROGRESS) {
	    logic.setResult(bResult);
	    logic.doResult();
	}
    }

    @Override
    public void run() {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    final var b = logic;
	    if (app.getMode() == StuffBag.STATUS_BATTLE && b instanceof MapTimeBattleLogic) {
		final var gui = logic.battleGUI;
		if (!gui.isEnemyActionBarFull()) {
		    gui.updateEnemyActionBarValue();
		    if (gui.isEnemyActionBarFull()) {
			this.enemyAct();
		    }
		} else {
		    this.enemyAct();
		}
	    }
	} catch (final Throwable t) {
	    Diane.handleError(t);
	}
    }
}