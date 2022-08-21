/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import javax.swing.JOptionPane;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class CustomDialogs {
    private CustomDialogs() {
	// Do nothing
    }

    public static int showDeadDialog() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	return JOptionPane
		.showOptionDialog(app.getOutputFrame(), Strings.dialog(DialogString.DEAD_MESSAGE),
			Strings.dialog(DialogString.DEAD_TITLE), JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.INFORMATION_MESSAGE, LogoLoader.getMicroLogo(),
			new String[] { Strings.dialog(DialogString.UNDO_BUTTON),
				Strings.dialog(DialogString.RESTART_BUTTON), Strings.dialog(DialogString.END_BUTTON) },
			Strings.dialog(DialogString.UNDO_BUTTON));
    }
}