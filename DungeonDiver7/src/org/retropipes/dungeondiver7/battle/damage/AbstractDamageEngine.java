/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.damage;

import org.retropipes.dungeondiver7.creature.AbstractCreature;
import org.retropipes.dungeondiver7.prefs.Prefs;

public abstract class AbstractDamageEngine {
	public static AbstractDamageEngine getEnemyInstance() {
		final var difficulty = Prefs.getGameDifficulty();
		return switch (difficulty) {
		case Prefs.DIFFICULTY_VERY_EASY -> new VeryHardDamageEngine();
		case Prefs.DIFFICULTY_EASY -> new HardDamageEngine();
		case Prefs.DIFFICULTY_NORMAL -> new NormalDamageEngine();
		case Prefs.DIFFICULTY_HARD -> new EasyDamageEngine();
		case Prefs.DIFFICULTY_VERY_HARD -> new VeryEasyDamageEngine();
		default -> new NormalDamageEngine();
		};
	}

	public static AbstractDamageEngine getPlayerInstance() {
		final var difficulty = Prefs.getGameDifficulty();
		return switch (difficulty) {
		case Prefs.DIFFICULTY_VERY_EASY -> new VeryEasyDamageEngine();
		case Prefs.DIFFICULTY_EASY -> new EasyDamageEngine();
		case Prefs.DIFFICULTY_NORMAL -> new NormalDamageEngine();
		case Prefs.DIFFICULTY_HARD -> new HardDamageEngine();
		case Prefs.DIFFICULTY_VERY_HARD -> new VeryHardDamageEngine();
		default -> new NormalDamageEngine();
		};
	}

	public abstract int computeDamage(AbstractCreature enemy, AbstractCreature acting);

	public abstract boolean enemyDodged();

	public abstract boolean weaponCrit();

	public abstract boolean weaponFumble();

	public abstract boolean weaponMissed();

	public abstract boolean weaponPierce();
}
