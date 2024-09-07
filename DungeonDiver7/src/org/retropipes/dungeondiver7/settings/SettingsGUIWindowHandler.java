package org.retropipes.dungeondiver7.settings;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class SettingsGUIWindowHandler extends WindowAdapter {
    private final SettingsGUI pm;
    
    public SettingsGUIWindowHandler(final SettingsGUI settingsGUI) {
        this.pm = settingsGUI;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        this.pm.hidePrefs();
    }
}