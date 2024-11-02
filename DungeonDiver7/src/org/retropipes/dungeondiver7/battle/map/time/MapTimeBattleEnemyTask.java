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
	this.logic = mapTimeBattleLogic;
    }

    private void enemyAct() {
	final var gui = this.logic.battleGUI;
	// Do Enemy Actions
	this.logic.executeNextAIAction();
	gui.resetEnemyActionBar();
	// Maintain Enemy Effects
	this.logic.maintainEffects(false);
	// Display Active Effects
	this.logic.displayActiveEffects();
	// Display End Stats
	this.logic.displayBattleStats();
	// Check Result
	final var bResult = this.logic.getResult();
	if (bResult != BattleResult.IN_PROGRESS) {
	    this.logic.setResult(bResult);
	    this.logic.doResult();
	}
    }

    @Override
    public void run() {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    if (app.getMode() == StuffBag.STATUS_BATTLE) {
		final var gui = this.logic.battleGUI;
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