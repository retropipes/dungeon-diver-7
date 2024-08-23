package org.retropipes.dungeondiver7.prefs;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class PrefsGUIWindowHandler extends WindowAdapter {
    private final PrefsGUI pm;
    
    public PrefsGUIWindowHandler(final PrefsGUI prefsGUI) {
        this.pm = prefsGUI;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        this.pm.hidePrefs();
    }
}