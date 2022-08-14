/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;

public class FileExtensions {
    private FileExtensions() {
	// Do nothing
    }

    // Constants
    private static final String PREFERENCES_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_PREFS);
    private static final String OLD_LEVEL_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_OLD_LEVEL);
    private static final String OLD_PLAYBACK_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_OLD_PLAYBACK);
    private static final String DUNGEON_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_ARENA);
    private static final String PROTECTED_DUNGEON_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_PROTECTED_ARENA);
    private static final String DUNGEON_LEVEL_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_DUNGEON_DATA);
    private static final String SAVED_GAME_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_SAVED_GAME);
    private static final String SCORES_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_SCORES);
    private static final String SOLUTION_EXTENSION = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_EXTENSION_SOLUTION);
    private static final String CHARACTER_EXTENSION = "chrxml";
    private static final String REGISTRY_EXTENSION = "regtxt";
    private static final String INTERNAL_DATA_EXTENSION = "txt";
    private static final String MUSIC_EXTENSION = "ogg";
    private static final String SOUND_EXTENSION = "wav";
    private static final String STRING_EXTENSION = "properties";

    // Methods
    public static String getPreferencesExtension() {
	return FileExtensions.PREFERENCES_EXTENSION;
    }

    public static String getOldLevelExtension() {
	return FileExtensions.OLD_LEVEL_EXTENSION;
    }

    public static String getOldPlaybackExtension() {
	return FileExtensions.OLD_PLAYBACK_EXTENSION;
    }

    public static String getDungeonExtension() {
	return FileExtensions.DUNGEON_EXTENSION;
    }

    public static String getDungeonExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + FileExtensions.DUNGEON_EXTENSION;
    }

    public static String getProtectedDungeonExtension() {
	return FileExtensions.PROTECTED_DUNGEON_EXTENSION;
    }

    public static String getProtectedDungeonExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + FileExtensions.PROTECTED_DUNGEON_EXTENSION;
    }

    public static String getDungeonLevelExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + FileExtensions.DUNGEON_LEVEL_EXTENSION;
    }

    public static String getGameExtension() {
	return FileExtensions.SAVED_GAME_EXTENSION;
    }

    public static String getGameExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + FileExtensions.SAVED_GAME_EXTENSION;
    }

    public static String getScoresExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + FileExtensions.SCORES_EXTENSION;
    }

    public static String getSolutionExtensionWithPeriod() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + FileExtensions.SOLUTION_EXTENSION;
    }

    public static String getCharacterExtension() {
	return FileExtensions.CHARACTER_EXTENSION;
    }

    public static String getCharacterExtensionWithPeriod() {
	return "." + FileExtensions.CHARACTER_EXTENSION;
    }

    public static String getRegistryExtensionWithPeriod() {
	return "." + FileExtensions.REGISTRY_EXTENSION;
    }

    public static String getInternalDataExtensionWithPeriod() {
	return "." + FileExtensions.INTERNAL_DATA_EXTENSION;
    }

    public static String getMusicExtensionWithPeriod() {
	return "." + FileExtensions.MUSIC_EXTENSION;
    }

    public static String getSoundExtensionWithPeriod() {
	return "." + FileExtensions.SOUND_EXTENSION;
    }

    public static String getStringExtensionWithPeriod() {
	return "." + FileExtensions.STRING_EXTENSION;
    }
}