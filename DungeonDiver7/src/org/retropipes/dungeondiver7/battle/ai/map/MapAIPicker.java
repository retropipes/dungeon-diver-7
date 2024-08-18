/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.ai.map;

import org.retropipes.dungeondiver7.prefs.Prefs;

public final class MapAIPicker {
    public static MapAI getNextRoutine() {
	final var difficulty = Prefs.getGameDifficulty();
	if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
	    return new VeryEasyMapAI();
	}
	if (difficulty == Prefs.DIFFICULTY_EASY) {
	    return new EasyMapAI();
	}
	if (difficulty == Prefs.DIFFICULTY_NORMAL) {
	    return new NormalMapAI();
	}
	if (difficulty == Prefs.DIFFICULTY_HARD) {
	    return new HardMapAI();
	}
	if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
	    return new VeryHardMapAI();
	}
	return new NormalMapAI();
    }

    // Constructors
    private MapAIPicker() {
	// Do nothing
    }
}
