/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.ai.map;

import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.battle.ai.AIContext;

public class AutoMapAI extends MapAI {
    // Constructor
    public AutoMapAI() {
    }

    @Override
    public BattleAction getNextAction(final AIContext ac) {
	final var there = ac.isEnemyNearby();
	if (there != null) {
	    // Something hostile is nearby, so attack it
	    this.moveX = there.x;
	    this.moveY = there.y;
	    return BattleAction.MOVE;
	}
	return BattleAction.END_TURN;
    }
}
