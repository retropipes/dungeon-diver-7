package org.retropipes.dungeondiver7.prefs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Strings;

class PrefsGUIActionHandler implements ActionListener {
    private final PrefsGUI pm;

    public PrefsGUIActionHandler(final PrefsGUI prefsGUI) {
        this.pm = prefsGUI;
    }

    // Handle buttons
    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
    	final var cmd = e.getActionCommand();
    	if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
    	    pm.setPrefs();
    	} else if (cmd.equals(Strings.dialog(DialogString.CANCEL_BUTTON))) {
    	    pm.hidePrefs();
    	}
        } catch (final Exception ex) {
    	DungeonDiver7.logError(ex);
        }
    }
}