/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.ErrorString;
import com.puttysoftware.dungeondiver7.locale.GameString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.fileutils.ResourceStreamReader;

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
	try (InputStream is = CheatManager.class.getResourceAsStream(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_CHEATS_PATH));
		ResourceStreamReader rsr = new ResourceStreamReader(is)) {
	    String line = Strings.EMPTY;
	    while (line != null) {
		line = rsr.readString();
		if (line != null) {
		    this.cheatCache.add(line);
		    this.cheatCount++;
		}
	    }
	    rsr.close();
	    is.close();
	} catch (final IOException e) {
	    // Ignore
	} catch (final NullPointerException e) {
	    // Ignore
	}
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
