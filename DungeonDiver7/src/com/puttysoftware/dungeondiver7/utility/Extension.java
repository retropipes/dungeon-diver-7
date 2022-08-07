/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;

public class Extension {
    private Extension() {
	// Do nothing
    }

    // Constants
    private static final String PREFERENCES_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_PREFS);
    private static final String OLD_LEVEL_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_OLD_LEVEL);
    private static final String OLD_PLAYBACK_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_OLD_PLAYBACK);
    private static final String ARENA_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_ARENA);
    private static final String PROTECTED_ARENA_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_PROTECTED_ARENA);
    private static final String ARENA_LEVEL_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_ARENA_DATA);
    private static final String SAVED_GAME_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_SAVED_GAME);
    private static final String SCORES_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_SCORES);
    private static final String SOLUTION_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_SOLUTION);

    // Methods
    public static String getPreferencesExtension() {
	return Extension.PREFERENCES_EXTENSION;
    }

    public static String getOldLevelExtension() {
	return Extension.OLD_LEVEL_EXTENSION;
    }

    public static String getOldPlaybackExtension() {
	return Extension.OLD_PLAYBACK_EXTENSION;
    }

    public static String getDungeonExtension() {
	return Extension.ARENA_EXTENSION;
    }

    public static String getDungeonExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.ARENA_EXTENSION;
    }

    public static String getProtectedDungeonExtension() {
	return Extension.PROTECTED_ARENA_EXTENSION;
    }

    public static String getProtectedDungeonExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.PROTECTED_ARENA_EXTENSION;
    }

    public static String getDungeonLevelExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.ARENA_LEVEL_EXTENSION;
    }

    public static String getGameExtension() {
	return Extension.SAVED_GAME_EXTENSION;
    }

    public static String getGameExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.SAVED_GAME_EXTENSION;
    }

    public static String getScoresExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.SCORES_EXTENSION;
    }

    public static String getSolutionExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.SOLUTION_EXTENSION;
    }
}