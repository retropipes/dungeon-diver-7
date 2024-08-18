/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.ai.window;

import org.retropipes.dungeondiver7.prefs.Prefs;

public final class WindowAIPicker {
    // Methods
    public static WindowAI getNextRoutine() {
        final var difficulty = Prefs.getGameDifficulty();
        switch (difficulty) {
        case Prefs.DIFFICULTY_VERY_EASY:
            return new VeryEasyWindowAI();
        case Prefs.DIFFICULTY_EASY:
            return new EasyWindowAI();
        case Prefs.DIFFICULTY_NORMAL:
            return new NormalWindowAI();
        case Prefs.DIFFICULTY_HARD:
            return new HardWindowAI();
        case Prefs.DIFFICULTY_VERY_HARD:
            return new VeryHardWindowAI();
        default:
            return new NormalWindowAI();
        }
    }
}
