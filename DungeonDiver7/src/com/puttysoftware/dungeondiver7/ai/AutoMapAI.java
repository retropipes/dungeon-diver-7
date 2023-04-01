/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.ai;

public class AutoMapAI extends AbstractMapAIRoutine {
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
            return AbstractMapAIRoutine.ACTION_MOVE;
        }
        return AbstractMapAIRoutine.ACTION_END_TURN;
    }
}
