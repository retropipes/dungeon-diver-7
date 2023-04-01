/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle.damage;

import com.puttysoftware.dungeondiver7.creature.AbstractCreature;
import com.puttysoftware.dungeondiver7.prefs.Prefs;

public abstract class AbstractDamageEngine {
	public abstract int computeDamage(AbstractCreature enemy, AbstractCreature acting);

	public abstract boolean enemyDodged();

	public abstract boolean weaponMissed();

	public abstract boolean weaponCrit();

	public abstract boolean weaponPierce();

	public abstract boolean weaponFumble();

	public static AbstractDamageEngine getPlayerInstance() {
		final var difficulty = Prefs.getGameDifficulty();
		switch (difficulty) {
			case Prefs.DIFFICULTY_VERY_EASY:
				return new VeryEasyDamageEngine();
			case Prefs.DIFFICULTY_EASY:
				return new EasyDamageEngine();
			case Prefs.DIFFICULTY_NORMAL:
				return new NormalDamageEngine();
			case Prefs.DIFFICULTY_HARD:
				return new HardDamageEngine();
			case Prefs.DIFFICULTY_VERY_HARD:
				return new VeryHardDamageEngine();
			default:
				return new NormalDamageEngine();
		}
	}

	public static AbstractDamageEngine getEnemyInstance() {
		final var difficulty = Prefs.getGameDifficulty();
		switch (difficulty) {
			case Prefs.DIFFICULTY_VERY_EASY:
				return new VeryHardDamageEngine();
			case Prefs.DIFFICULTY_EASY:
				return new HardDamageEngine();
			case Prefs.DIFFICULTY_NORMAL:
				return new NormalDamageEngine();
			case Prefs.DIFFICULTY_HARD:
				return new EasyDamageEngine();
			case Prefs.DIFFICULTY_VERY_HARD:
				return new VeryEasyDamageEngine();
			default:
				return new NormalDamageEngine();
		}
	}
}
