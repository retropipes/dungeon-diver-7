/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;

class HelpManager {
    // Fields
    private MainWindow mainWindow;
    private boolean inited = false;

    // Constructors
    public HelpManager() {
	// Do nothing
    }

    // Methods
    void showHelp() {
	this.initHelp();
	DungeonDiver7.getStuffBag().setInHelp();
    }

    void activeLanguageChanged() {
	this.inited = false;
    }

    private void initHelp() {
	if (!this.inited) {
	    this.mainWindow = MainWindow.mainWindow();
	    final var iconlogo = LogoLoader.getIconLogo();
	    this.mainWindow.setSystemIcon(iconlogo);
	    this.inited = true;
	}
    }
}
