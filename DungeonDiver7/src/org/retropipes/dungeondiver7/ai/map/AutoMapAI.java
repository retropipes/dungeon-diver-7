/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.ai.map;

import org.retropipes.dungeondiver7.ai.AIContext;
import org.retropipes.dungeondiver7.ai.BattleAction;

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
