/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.ai.map;

public class AutoMapAI extends MapAI {
    // Constructor
    public AutoMapAI() {
    }

    @Override
    public int getNextAction(final MapAIContext ac) {
	final var there = ac.isEnemyNearby();
	if (there != null) {
	    // Something hostile is nearby, so attack it
	    this.moveX = there.x;
	    this.moveY = there.y;
	    return MapAI.ACTION_MOVE;
	}
	return MapAI.ACTION_END_TURN;
    }
}
