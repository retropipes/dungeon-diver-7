/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.damage;

import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.GameDifficulty;
import org.retropipes.dungeondiver7.settings.Settings;

public abstract class DamageEngine {
    public static DamageEngine getEnemyInstance() {
	final var difficulty = Settings.getGameDifficulty();
	return switch (difficulty) {
	case GameDifficulty.VERY_EASY -> new VeryHardDamageEngine();
	case GameDifficulty.EASY -> new HardDamageEngine();
	case GameDifficulty.NORMAL -> new NormalDamageEngine();
	case GameDifficulty.HARD -> new EasyDamageEngine();
	case GameDifficulty.VERY_HARD -> new VeryEasyDamageEngine();
	default -> new NormalDamageEngine();
	};
    }

    public static DamageEngine getPlayerInstance() {
	final var difficulty = Settings.getGameDifficulty();
	return switch (difficulty) {
	case GameDifficulty.VERY_EASY -> new VeryEasyDamageEngine();
	case GameDifficulty.EASY -> new EasyDamageEngine();
	case GameDifficulty.NORMAL -> new NormalDamageEngine();
	case GameDifficulty.HARD -> new HardDamageEngine();
	case GameDifficulty.VERY_HARD -> new VeryHardDamageEngine();
	default -> new NormalDamageEngine();
	};
    }

    public abstract int computeDamage(Creature enemy, Creature acting);

    public abstract boolean enemyDodged();

    public abstract boolean weaponCrit();

    public abstract boolean weaponFumble();

    public abstract boolean weaponMissed();

    public abstract boolean weaponPierce();
}
