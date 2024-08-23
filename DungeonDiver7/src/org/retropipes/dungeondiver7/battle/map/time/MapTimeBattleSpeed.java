/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.map.time;

import org.retropipes.dungeondiver7.prefs.Prefs;

class MapTimeBattleSpeed {
    // Method
    static int getSpeed() {
	return Prefs.getBattleSpeed() / 100;
    }

    // Constructor
    private MapTimeBattleSpeed() {
	// Do nothing
    }
}