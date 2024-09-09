/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.
All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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