/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Strings;

class SettingsGUIActionHandler implements ActionListener {
    private final SettingsGUI pm;

    public SettingsGUIActionHandler(final SettingsGUI settingsGUI) {
	this.pm = settingsGUI;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    final var cmd = e.getActionCommand();
	    if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
		this.pm.setPrefs();
	    } else if (cmd.equals(Strings.dialog(DialogString.CANCEL_BUTTON))) {
		this.pm.hidePrefs();
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }
}