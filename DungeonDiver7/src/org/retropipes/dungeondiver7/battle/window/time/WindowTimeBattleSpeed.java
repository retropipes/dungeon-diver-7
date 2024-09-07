/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.window.time;

import org.retropipes.dungeondiver7.settings.Settings;

class WindowTimeBattleSpeed {
    // Method
    static int getSpeed() {
	return Settings.getBattleSpeed() / 10;
    }

    // Constructor
    private WindowTimeBattleSpeed() {
	// Do nothing
    }
}