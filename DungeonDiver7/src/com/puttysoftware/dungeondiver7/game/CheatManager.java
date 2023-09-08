/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import java.util.ArrayList;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.ErrorString;
import com.puttysoftware.dungeondiver7.locale.GameString;
import com.puttysoftware.dungeondiver7.locale.Strings;

final class CheatManager {
    // Fields
    private final ArrayList<String> cheatCache;
    private final int cheatCount;

    // Constructor
    public CheatManager() {
	this.cheatCount = 0;
	this.cheatCache = new ArrayList<>();
	this.loadCheatCache();
    }

    String enterCheat() {
	final var userInput = CommonDialogs.showTextInputDialog(Strings.game(GameString.CHEAT_PROMPT),
		Strings.dialog(DialogString.CHEATS));
	if (userInput == null) {
	    return null;
	}
	final var index = this.cheatCache.indexOf(userInput.toLowerCase());
	if (index == -1) {
	    CommonDialogs.showErrorDialog(Strings.error(ErrorString.INVALID_CHEAT),
		    Strings.dialog(DialogString.CHEATS));
	    return null;
	}
	final var value = CommonDialogs.showConfirmDialog(Strings.game(GameString.CHEAT_ACTION),
		Strings.dialog(DialogString.CHEATS));
	if (value == CommonDialogs.YES_OPTION) {
	    return Strings.game(GameString.ENABLE_CHEAT) + Strings.SPACE + userInput.toLowerCase();
	}
	return Strings.game(GameString.DISABLE_CHEAT) + Strings.SPACE + userInput.toLowerCase();
    }

    int getCheatCount() {
	return this.cheatCount;
    }

    private void loadCheatCache() {
	this.cheatCache.addAll(Strings.allCheats());
    }

    int queryCheatCache(final String query) {
	return this.cheatCache.indexOf(query);
    }
}
