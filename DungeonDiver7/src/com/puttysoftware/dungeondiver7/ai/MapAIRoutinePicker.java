/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.ai;

import com.puttysoftware.dungeondiver7.prefs.PrefsManager;

public final class MapAIRoutinePicker {
    // Constructors
    private MapAIRoutinePicker() {
	// Do nothing
    }

    // Methods
    public static AbstractMapAIRoutine getNextRoutine() {
	final int difficulty = PrefsManager.getGameDifficulty();
	if (difficulty == PrefsManager.DIFFICULTY_VERY_EASY) {
	    return new VeryEasyMapAIRoutine();
	} else if (difficulty == PrefsManager.DIFFICULTY_EASY) {
	    return new EasyMapAIRoutine();
	} else if (difficulty == PrefsManager.DIFFICULTY_NORMAL) {
	    return new NormalMapAIRoutine();
	} else if (difficulty == PrefsManager.DIFFICULTY_HARD) {
	    return new HardMapAIRoutine();
	} else if (difficulty == PrefsManager.DIFFICULTY_VERY_HARD) {
	    return new VeryHardMapAIRoutine();
	} else {
	    return new NormalMapAIRoutine();
	}
    }
}