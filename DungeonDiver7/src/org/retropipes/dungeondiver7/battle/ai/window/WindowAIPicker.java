/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.ai.window;

import org.retropipes.dungeondiver7.creature.GameDifficulty;
import org.retropipes.dungeondiver7.settings.Settings;

public final class WindowAIPicker {
    // Methods
    public static WindowAI getNextRoutine() {
	final var difficulty = Settings.getGameDifficulty();
	switch (difficulty) {
	case GameDifficulty.VERY_EASY:
	    return new VeryEasyWindowAI();
	case GameDifficulty.EASY:
	    return new EasyWindowAI();
	case GameDifficulty.NORMAL:
	    return new NormalWindowAI();
	case GameDifficulty.HARD:
	    return new HardWindowAI();
	case GameDifficulty.VERY_HARD:
	    return new VeryHardWindowAI();
	default:
	    return new NormalWindowAI();
	}
    }
}
