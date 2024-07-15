/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import org.retropipes.diane.gui.dialog.CommonDialogs;

import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class CustomDialogs {
	public static int showDeadDialog() {
		return CommonDialogs.showCustomDialogWithDefault(Strings.dialog(DialogString.DEAD_MESSAGE),
				Strings.dialog(DialogString.DEAD_TITLE), new String[] { Strings.dialog(DialogString.UNDO_BUTTON),
						Strings.dialog(DialogString.RESTART_BUTTON), Strings.dialog(DialogString.END_BUTTON) },
				Strings.dialog(DialogString.UNDO_BUTTON));
	}

	private CustomDialogs() {
		// Do nothing
	}
}