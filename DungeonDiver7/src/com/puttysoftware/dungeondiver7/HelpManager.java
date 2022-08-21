/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.strings.DianeStrings;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

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
	    this.helpFrame = new JFrame(DianeStrings.subst(Strings.dialog(DialogString.HELP),
		    Strings.untranslated(Untranslated.PROGRAM_NAME)));
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
