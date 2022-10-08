/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.BorderLayout;

import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.strings.DianeStrings;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

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
	this.mainWindow.setVisible(true);
    }

    void activeLanguageChanged() {
	this.inited = false;
    }

    private void initHelp() {
	if (!this.inited) {
	    this.mainWindow = MainWindow.mainWindow();
	    this.mainWindow.setTitle(DianeStrings.subst(Strings.dialog(DialogString.HELP),
		    Strings.untranslated(Untranslated.PROGRAM_NAME)));
	    final var iconlogo = LogoLoader.getIconLogo();
	    this.mainWindow.setSystemIcon(iconlogo);
	    this.mainWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	    this.mainWindow.setLayout(new BorderLayout());
	    this.mainWindow.pack();
	    this.mainWindow.setResizable(false);
	    this.inited = true;
	}
    }
}
