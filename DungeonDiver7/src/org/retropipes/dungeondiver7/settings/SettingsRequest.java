/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.
All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.settings;

import java.awt.desktop.PreferencesEvent;
import java.awt.desktop.PreferencesHandler;

public class SettingsRequest implements PreferencesHandler {
    public SettingsRequest() {
	// Do nothing
    }

    @Override
    public void handlePreferences(final PreferencesEvent pe) {
	Settings.showPrefs();
    }
}