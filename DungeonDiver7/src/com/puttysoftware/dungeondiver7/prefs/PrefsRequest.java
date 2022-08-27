/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.
All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.prefs;

import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

public class PrefsRequest implements PreferencesHandler {
    public PrefsRequest() {
	// Do nothing
    }

    @Override
    public void handlePreferences(final PreferencesEvent pe) {
	Prefs.showPrefs();
    }
}