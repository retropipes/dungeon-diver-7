/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.ai.map;

import org.retropipes.dungeondiver7.creature.GameDifficulty;
import org.retropipes.dungeondiver7.settings.Settings;

public final class MapAIPicker {
    public static MapAI getNextRoutine() {
	final var difficulty = Settings.getGameDifficulty();
	if (difficulty == GameDifficulty.VERY_EASY) {
	    return new VeryEasyMapAI();
	}
	if (difficulty == GameDifficulty.EASY) {
	    return new EasyMapAI();
	}
	if (difficulty == GameDifficulty.NORMAL) {
	    return new NormalMapAI();
	}
	if (difficulty == GameDifficulty.HARD) {
	    return new HardMapAI();
	}
	if (difficulty == GameDifficulty.VERY_HARD) {
	    return new VeryHardMapAI();
	}
	return new NormalMapAI();
    }

    // Constructors
    private MapAIPicker() {
	// Do nothing
    }
}
