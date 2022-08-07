/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.prefs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.EditorLayoutConstants;
import com.puttysoftware.dungeondiver7.utility.Extension;

public class PrefsManager {
    // Fields
    private final static PrefsFileManager storeMgr = new PrefsFileManager();
    private final static PrefsGUIManager guiMgr = new PrefsGUIManager();
    private final static int FALLBACK_LANGUAGE_ID = 0;
    private final static int DEFAULT_EDITOR_LAYOUT_ID = EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V12;

    // Private constructor
    private PrefsManager() {
	// Do nothing
    }

    // Methods
    public static void activeLanguageChanged() {
	PrefsManager.guiMgr.activeLanguageChanged();
    }

    public static int getEditorLayoutID() {
	return PrefsManager.storeMgr.getInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_EDITOR_LAYOUT_ID), PrefsManager.DEFAULT_EDITOR_LAYOUT_ID);
    }

    public static void setEditorLayoutID(final int value) {
	PrefsManager.storeMgr.setInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_EDITOR_LAYOUT_ID), value);
	DungeonDiver7.getApplication().getEditor().resetBorderPane();
    }

    public static boolean getEditorShowAllObjects() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_EDITOR_SHOW_ALL), true);
    }

    public static void setEditorShowAllObjects(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_EDITOR_SHOW_ALL), value);
	DungeonDiver7.getApplication().getEditor().resetBorderPane();
    }

    public static int getLanguageID() {
	return PrefsManager.storeMgr.getInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_LANGUAGE_ID), PrefsManager.FALLBACK_LANGUAGE_ID);
    }

    public static void setLanguageID(final int value) {
	final int oldValue = PrefsManager.getLanguageID();
	PrefsManager.storeMgr.setInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_LANGUAGE_ID), value);
	if (oldValue != value) {
	    LocaleLoader.activeLanguageChanged(value);
	}
    }

    public static boolean useClassicAccelerators() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_CLASSIC_ACCEL), false);
    }

    public static void setClassicAccelerators(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_CLASSIC_ACCEL), value);
    }

    public static String getLastDirOpen() {
	return PrefsManager.storeMgr.getString(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_LAST_DIR_OPEN), LocaleConstants.COMMON_STRING_EMPTY);
    }

    public static void setLastDirOpen(final String value) {
	PrefsManager.storeMgr.setString(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_LAST_DIR_OPEN), value);
    }

    public static String getLastDirSave() {
	return PrefsManager.storeMgr.getString(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_LAST_DIR_SAVE), LocaleConstants.COMMON_STRING_EMPTY);
    }

    public static void setLastDirSave(final String value) {
	PrefsManager.storeMgr.setString(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_LAST_DIR_SAVE), value);
    }

    public static boolean shouldCheckUpdatesAtStartup() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_UPDATES_STARTUP), true);
    }

    static void setCheckUpdatesAtStartup(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_UPDATES_STARTUP), value);
    }

    static int getActionDelay() {
	return PrefsManager.storeMgr.getInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ACTION_DELAY), 2);
    }

    static void setActionDelay(final int value) {
	PrefsManager.storeMgr.setInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ACTION_DELAY), value);
    }

    public static long getActionSpeed() {
	return (PrefsManager.getActionDelay() + 1) * 5;
    }

    public static long getReplaySpeed() {
	return (PrefsManager.getActionDelay() + 1) * 10;
    }

    public static boolean oneMove() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ONE_MOVE), true);
    }

    static void setOneMove(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ONE_MOVE), value);
    }

    public static boolean enableAnimation() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_ANIMATION), true);
    }

    static void setEnableAnimation(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_ANIMATION), value);
    }

    public static boolean isKidsDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_KIDS), true);
    }

    public static void setKidsDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_KIDS), value);
    }

    public static boolean isEasyDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_EASY), true);
    }

    public static void setEasyDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_EASY), value);
    }

    public static boolean isMediumDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_MEDIUM), true);
    }

    public static void setMediumDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_MEDIUM), value);
    }

    public static boolean isHardDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_HARD), true);
    }

    public static void setHardDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_HARD), value);
    }

    public static boolean isDeadlyDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_DEADLY), true);
    }

    public static void setDeadlyDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_DEADLY), value);
    }

    public static AbstractDungeonObject getEditorDefaultFill() {
	return new Ground();
    }

    public static boolean getSoundsEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_SOUNDS), true);
    }

    static void setSoundsEnabled(final boolean status) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_SOUNDS), status);
    }

    public static boolean getMusicEnabled() {
	return PrefsManager.storeMgr.getBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_MUSIC), true);
    }

    static void setMusicEnabled(final boolean status) {
	PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_MUSIC), status);
    }

    public static JFrame getPrefFrame() {
	return PrefsManager.guiMgr.getPrefFrame();
    }

    public static void showPrefs() {
	PrefsManager.guiMgr.showPrefs();
    }

    private static String getPrefsDirPrefix() {
	final String osName = System.getProperty(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME));
	if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_MAC_OS_X)) != -1) {
	    // Mac OS X
	    return System.getenv(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_DIRECTORY_UNIX_HOME));
	} else if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_WINDOWS)) != -1) {
	    // Windows
	    return System.getenv(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_DIRECTORY_WINDOWS_APPDATA));
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_DIRECTORY_UNIX_HOME));
	}
    }

    private static String getPrefsDirectory() {
	final String osName = System.getProperty(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME));
	if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_MAC_OS_X)) != -1) {
	    // Mac OS X
	    return LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_DIRECTORY_PREFS_MAC);
	} else if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_WINDOWS)) != -1) {
	    // Windows
	    return LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_DIRECTORY_PREFS_WINDOWS);
	} else {
	    // Other - assume UNIX-like
	    return LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_DIRECTORY_PREFS_UNIX);
	}
    }

    private static String getPrefsFileExtension() {
	return LocaleConstants.COMMON_STRING_NOTL_PERIOD + Extension.getPreferencesExtension();
    }

    private static String getPrefsFileName() {
	final String osName = System.getProperty(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME));
	if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_MAC_OS_X)) != -1) {
	    // Mac OS X
	    return LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_FILE_PREFS_MAC);
	} else if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_WINDOWS)) != -1) {
	    // Windows
	    return LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_FILE_PREFS_WINDOWS);
	} else {
	    // Other - assume UNIX-like
	    return LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_FILE_PREFS_UNIX);
	}
    }

    private static String getPrefsFile() {
	final StringBuilder b = new StringBuilder();
	b.append(PrefsManager.getPrefsDirPrefix());
	b.append(PrefsManager.getPrefsDirectory());
	b.append(PrefsManager.getPrefsFileName());
	b.append(PrefsManager.getPrefsFileExtension());
	return b.toString();
    }

    public static void writePrefs() {
	try (BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(PrefsManager.getPrefsFile()))) {
	    PrefsManager.storeMgr.saveStore(buf);
	} catch (final IOException io) {
	    // Ignore
	}
    }

    public static void readPrefs() {
	try (BufferedInputStream buf = new BufferedInputStream(new FileInputStream(PrefsManager.getPrefsFile()))) {
	    // Read new preferences
	    PrefsManager.storeMgr.loadStore(buf);
	} catch (final IOException io) {
	    // Populate store with defaults
	    PrefsManager.storeMgr.setString(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_LAST_DIR_OPEN), LocaleConstants.COMMON_STRING_EMPTY);
	    PrefsManager.storeMgr.setString(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_LAST_DIR_SAVE), LocaleConstants.COMMON_STRING_EMPTY);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_UPDATES_STARTUP), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ONE_MOVE), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_SOUNDS), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_MUSIC), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_ANIMATION), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_KIDS), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_EASY), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_MEDIUM), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_HARD), true);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ENABLE_DIFFICULTY_DEADLY), true);
	    PrefsManager.storeMgr.setInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_ACTION_DELAY), 2);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_CLASSIC_ACCEL), false);
	    PrefsManager.storeMgr.setInteger(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_LANGUAGE_ID), PrefsManager.FALLBACK_LANGUAGE_ID);
	    PrefsManager.storeMgr.setInteger(
		    LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			    LocaleConstants.NOTL_STRING_PREFS_KEY_EDITOR_LAYOUT_ID),
		    PrefsManager.DEFAULT_EDITOR_LAYOUT_ID);
	    PrefsManager.storeMgr.setBoolean(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_PREFS_KEY_EDITOR_SHOW_ALL), true);
	}
    }
}
