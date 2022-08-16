/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.Globals;
import com.puttysoftware.dungeondiver7.locale.GlobalsUntranslated;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;

class HelpManager {
    // Fields
    private JFrame helpFrame;
    private boolean inited = false;

    // Constructors
    public HelpManager() {
	// Do nothing
    }

    // Methods
    void showHelp() {
	this.initHelp();
	DungeonDiver7.getStuffBag().setInHelp();
	this.helpFrame.setVisible(true);
    }

    void activeLanguageChanged() {
	this.inited = false;
    }

    private void initHelp() {
	if (!this.inited) {
	    this.helpFrame = new JFrame(Globals.untranslated(GlobalsUntranslated.PROGRAM_NAME)
		    + LocaleConstants.COMMON_STRING_SPACE
		    + LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_HELP));
	    final Image iconlogo = LogoLoader.getIconLogo();
	    this.helpFrame.setIconImage(iconlogo);
	    this.helpFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	    this.helpFrame.setLayout(new BorderLayout());
	    this.helpFrame.pack();
	    this.helpFrame.setResizable(false);
	    this.inited = true;
	}
    }
}
