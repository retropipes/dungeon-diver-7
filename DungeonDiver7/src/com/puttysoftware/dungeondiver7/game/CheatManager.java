/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.ErrorString;
import com.puttysoftware.dungeondiver7.locale.GameString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;

final class CheatManager {
    // Fields
    private final ArrayList<String> cheatCache;
    private int cheatCount;

    // Constructor
    public CheatManager() {
	this.cheatCount = 0;
	this.cheatCache = new ArrayList<>();
	this.loadCheatCache();
    }

    // Methods
    private void loadCheatCache() {
	this.cheatCache.addAll(Strings.allCheats());
    }

    String enterCheat() {
	final String userInput = CommonDialogs.showTextInputDialog(Strings.game(GameString.CHEAT_PROMPT),
		Strings.dialog(DialogString.CHEATS));
	if (userInput != null) {
	    final int index = this.cheatCache.indexOf(userInput.toLowerCase());
	    if (index != -1) {
		final int value = CommonDialogs.showConfirmDialog(Strings.game(GameString.CHEAT_ACTION),
			Strings.dialog(DialogString.CHEATS));
		if (value == JOptionPane.YES_OPTION) {
		    return Strings.game(GameString.ENABLE_CHEAT) + LocaleConstants.COMMON_STRING_SPACE
			    + userInput.toLowerCase();
		} else {
		    return Strings.game(GameString.DISABLE_CHEAT) + LocaleConstants.COMMON_STRING_SPACE
			    + userInput.toLowerCase();
		}
	    } else {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.INVALID_CHEAT),
			Strings.dialog(DialogString.CHEATS));
		return null;
	    }
	} else {
	    return null;
	}
    }

    int getCheatCount() {
	return this.cheatCount;
    }

    int queryCheatCache(final String query) {
	return this.cheatCache.indexOf(query);
    }
}
