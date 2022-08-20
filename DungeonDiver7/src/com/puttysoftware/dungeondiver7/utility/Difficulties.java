/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.Difficulty;
import com.puttysoftware.dungeondiver7.locale.Strings;

public final class Difficulties {
    // Constants
    public static final int KIDS = 1;
    public static final int EASY = 2;
    public static final int MEDIUM = 3;
    public static final int HARD = 4;
    public static final int DEADLY = 5;
    private static String[] NAMES = null;

    // Private Constructor
    private Difficulties() {
	// Do nothing
    }

    public static String[] getDifficultyNames() {
	if (Difficulties.NAMES == null) {
	    Difficulties.activeLanguageChanged();
	}
	return Difficulties.NAMES;
    }

    public static void activeLanguageChanged() {
	Difficulties.NAMES = new String[] { Strings.difficulty(Difficulty.KIDS), Strings.difficulty(Difficulty.EASY),
		Strings.difficulty(Difficulty.MEDIUM), Strings.difficulty(Difficulty.HARD),
		Strings.difficulty(Difficulty.DEADLY) };
    }
}
