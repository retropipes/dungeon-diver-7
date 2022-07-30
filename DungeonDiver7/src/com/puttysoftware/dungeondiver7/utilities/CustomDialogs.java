/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utilities;

import javax.swing.JOptionPane;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;

public class CustomDialogs {
    private CustomDialogs() {
	// Do nothing
    }

    public static int showDeadDialog() {
	final Application app = DungeonDiver7.getApplication();
	return JOptionPane.showOptionDialog(app.getOutputFrame(),
		LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_DEAD_MESSAGE),
		LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_DEAD_TITLE),
		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, LogoLoader.getMicroLogo(),
		new String[] {
			LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
				LocaleConstants.DIALOG_STRING_UNDO_BUTTON),
			LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
				LocaleConstants.DIALOG_STRING_RESTART_BUTTON),
			LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
				LocaleConstants.DIALOG_STRING_END_BUTTON) },
		LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_UNDO_BUTTON));
    }
}