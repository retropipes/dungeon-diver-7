/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;

public final class EditorLayouts {
    // Constants
    public static final int EDITOR_LAYOUT_CLASSIC = 0;
    public static final int EDITOR_LAYOUT_MODERN_V11 = 1;
    public static final int EDITOR_LAYOUT_MODERN_V12 = 2;
    private static String[] EDITOR_LAYOUT_LIST = new String[] {
	    LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		    LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_CLASSIC),
	    LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		    LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_MODERN_V11),
	    LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		    LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_MODERN_V12) };

    // Private Constructor
    private EditorLayouts() {
	// Do nothing
    }

    public static void activeLanguageChanged() {
	EditorLayouts.EDITOR_LAYOUT_LIST = new String[] {
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
			LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_CLASSIC),
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
			LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_MODERN_V11),
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
			LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_MODERN_V12) };
    }

    public static String[] getEditorLayoutList() {
	return EditorLayouts.EDITOR_LAYOUT_LIST;
    }
}
