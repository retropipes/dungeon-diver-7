/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utilities;

import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;

public final class DifficultyConstants {
    // Constants
    public static final int DIFFICULTY_KIDS = 1;
    public static final int DIFFICULTY_EASY = 2;
    public static final int DIFFICULTY_MEDIUM = 3;
    public static final int DIFFICULTY_HARD = 4;
    public static final int DIFFICULTY_DEADLY = 5;
    private static String[] DIFFICULTY_NAMES = null;

    // Private Constructor
    private DifficultyConstants() {
	// Do nothing
    }

    public static String[] getDifficultyNames() {
	if (DifficultyConstants.DIFFICULTY_NAMES == null) {
	    DifficultyConstants.reloadDifficultyNames();
	}
	return DifficultyConstants.DIFFICULTY_NAMES;
    }

    public static void reloadDifficultyNames() {
	DifficultyConstants.DIFFICULTY_NAMES = new String[] {
		LocaleLoader.loadString(LocaleConstants.DIFFICULTY_STRINGS_FILE,
			LocaleConstants.DIFFICULTY_STRING_KIDS),
		LocaleLoader.loadString(LocaleConstants.DIFFICULTY_STRINGS_FILE,
			LocaleConstants.DIFFICULTY_STRING_EASY),
		LocaleLoader.loadString(LocaleConstants.DIFFICULTY_STRINGS_FILE,
			LocaleConstants.DIFFICULTY_STRING_MEDIUM),
		LocaleLoader.loadString(LocaleConstants.DIFFICULTY_STRINGS_FILE,
			LocaleConstants.DIFFICULTY_STRING_HARD),
		LocaleLoader.loadString(LocaleConstants.DIFFICULTY_STRINGS_FILE,
			LocaleConstants.DIFFICULTY_STRING_DEADLY) };
    }
}
