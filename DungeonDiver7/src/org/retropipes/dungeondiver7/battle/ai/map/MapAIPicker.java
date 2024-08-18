/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.ai.map;

import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.utility.GameDifficulty;

public final class MapAIPicker {
    public static MapAI getNextRoutine() {
	final var difficulty = Prefs.getGameDifficulty();
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
