/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.map.time;

import org.retropipes.dungeondiver7.prefs.Prefs;

class MapTimeBattleSpeed {
    // Constants
    private static int SPEED_FACTOR = 20;

    // Method
    static int getSpeed() {
	return Prefs.getBattleSpeed() / MapTimeBattleSpeed.SPEED_FACTOR;
    }

    // Constructor
    private MapTimeBattleSpeed() {
	// Do nothing
    }
}