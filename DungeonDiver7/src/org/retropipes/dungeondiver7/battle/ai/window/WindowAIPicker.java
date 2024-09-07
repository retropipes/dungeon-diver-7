/* RetroRPGCS: An RPG */
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
