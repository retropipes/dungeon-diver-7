/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.window.time;

import org.retropipes.dungeondiver7.prefs.Prefs;

class WindowTimeBattleSpeed {
    // Method
    static int getSpeed() {
	return Prefs.getBattleSpeed() / 10;
    }

    // Constructor
    private WindowTimeBattleSpeed() {
	// Do nothing
    }
}