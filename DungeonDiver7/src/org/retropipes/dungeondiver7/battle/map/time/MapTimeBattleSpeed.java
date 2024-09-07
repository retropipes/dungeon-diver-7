/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.map.time;

import org.retropipes.dungeondiver7.settings.Settings;

class MapTimeBattleSpeed {
    // Method
    static int getSpeed() {
	return Settings.getBattleSpeed() / 100;
    }

    // Constructor
    private MapTimeBattleSpeed() {
	// Do nothing
    }
}