/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
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