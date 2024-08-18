/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.window.time;

import org.retropipes.dungeondiver7.prefs.Prefs;

class WindowTimeBattleSpeed {
    // Constants
    private static int SPEED_FACTOR = 10;

    // Method
    static int getSpeed() {
	return Prefs.getBattleSpeed() / WindowTimeBattleSpeed.SPEED_FACTOR;
    }

    // Constructor
    private WindowTimeBattleSpeed() {
	// Do nothing
    }
}