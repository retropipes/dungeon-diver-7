/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.GameDifficulty;

public class MonsterFactory {
    public static Creature getNewFinalBossInstance(final GameDifficulty diff) {
	return new FinalBossMonster(diff);
    }

    public static Creature getNewMonsterInstance(final GameDifficulty diff) {
	return new NormalMonster(diff);
    }

    private MonsterFactory() {
	// Do nothing
    }
}
