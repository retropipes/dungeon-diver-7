/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.utility;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Strings;

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