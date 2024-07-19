/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import org.retropipes.dungeondiver7.creature.AbstractCreature;

public class MonsterFactory {
	public static AbstractCreature getNewBossInstance() {
		return new BossMonster();
	}

	public static AbstractCreature getNewFinalBossInstance() {
		return new FinalBossMonster();
	}

	public static AbstractCreature getNewMonsterInstance() {
		return new Monster();
	}

	private MonsterFactory() {
		// Do nothing
	}
}
