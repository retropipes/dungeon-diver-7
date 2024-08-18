/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import org.retropipes.dungeondiver7.creature.Creature;

public class MonsterFactory {
    public static Creature getNewFinalBossInstance() {
	return new FinalBossMonster();
    }

    public static Creature getNewMonsterInstance() {
	return new NormalMonster();
    }

    private MonsterFactory() {
	// Do nothing
    }
}
