/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.ai;

import org.retropipes.dungeondiver7.prefs.Prefs;

public final class MapAIRoutinePicker {
	public static AbstractMapAIRoutine getNextRoutine() {
		final var difficulty = Prefs.getGameDifficulty();
		if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
			return new VeryEasyMapAIRoutine();
		}
		if (difficulty == Prefs.DIFFICULTY_EASY) {
			return new EasyMapAIRoutine();
		}
		if (difficulty == Prefs.DIFFICULTY_NORMAL) {
			return new NormalMapAIRoutine();
		}
		if (difficulty == Prefs.DIFFICULTY_HARD) {
			return new HardMapAIRoutine();
		}
		if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
			return new VeryHardMapAIRoutine();
		}
		return new NormalMapAIRoutine();
	}

	// Constructors
	private MapAIRoutinePicker() {
		// Do nothing
	}
}
