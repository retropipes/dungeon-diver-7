/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.prefs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.locale.EditorLayout;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.PrefKey;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

public class PrefsManager {
    // Fields
    private static PrefsFileManager storeMgr;
    private static PrefsGUIManager guiMgr;
    private final static int FALLBACK_LANGUAGE = 0;
    private final static EditorLayout DEFAULT_EDITOR_LAYOUT = EditorLayout.VERTICAL;
    private static final int BATTLE_SPEED = 1000;
    private static final int VIEWING_WINDOW_SIZE = 11;
    public static final int DIFFICULTY_VERY_EASY = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_NORMAL = 2;
    public static final int DIFFICULTY_HARD = 3;
    public static final int DIFFICULTY_VERY_HARD = 4;
    private static final int DEFAULT_DIFFICULTY = PrefsManager.DIFFICULTY_NORMAL;

    // Private constructor
    private PrefsManager() {
	// Do nothing
    }

    // Methods
    public static void init() {
	PrefsManager.storeMgr = new PrefsFileManager();
	PrefsManager.guiMgr = new PrefsGUIManager();
    }

    public static int getBattleSpeed() {
	return PrefsManager.BATTLE_SPEED;
    }

    public static int getViewingWindowSize() {
	return PrefsManager.VIEWING_WINDOW_SIZE;
    }

    public static int getGameDifficulty() {
	return PrefsManager.storeMgr.getInteger(Strings.prefKey(PrefKey.GAME_DIFFICULTY),
		PrefsManager.DEFAULT_DIFFICULTY);
    }

    static void setGameDifficulty(final int value) {
	PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.GAME_DIFFICULTY), value);
    }

    public static void activeLanguageChanged() {
	PrefsManager.guiMgr.activeLanguageChanged();
    }

    public static EditorLayout getEditorLayout() {
	return EditorLayout.values()[PrefsManager.storeMgr.getInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT),
		PrefsManager.DEFAULT_EDITOR_LAYOUT.ordinal())];
    }

    public static void setEditorLayout(final EditorLayout value) {
	PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT), value.ordinal());
	DungeonDiver7.getStuffBag().getEditor().resetBorderPane();
    }

    public static boolean getEditorShowAllObjects() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), true);
    }

    public static void setEditorShowAllObjects(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), value);
	DungeonDiver7.getStuffBag().getEditor().resetBorderPane();
    }

    public static int getLanguageID() {
	return PrefsManager.storeMgr.getInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE),
		PrefsManager.FALLBACK_LANGUAGE);
    }

    public static void setLanguageID(final int value) {
	final int oldValue = PrefsManager.getLanguageID();
	PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), value);
	if (oldValue != value) {
	    Strings.changeLanguage(Locale.getDefault());
	}
    }

    public static boolean useClassicAccelerators() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), false);
    }

    public static void setClassicAccelerators(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), value);
    }

    public static String getLastDirOpen() {
	return PrefsManager.storeMgr.getString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), Strings.EMPTY);
    }

    public static void setLastDirOpen(final String value) {
	PrefsManager.storeMgr.setString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), value);
    }

    public static String getLastDirSave() {
	return PrefsManager.storeMgr.getString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), Strings.EMPTY);
    }

    public static void setLastDirSave(final String value) {
	PrefsManager.storeMgr.setString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), value);
    }

    public static boolean shouldCheckUpdatesAtStartup() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), true);
    }

    static void setCheckUpdatesAtStartup(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), value);
    }

    static int getActionDelay() {
	return PrefsManager.storeMgr.getInteger(Strings.prefKey(PrefKey.ACTION_DELAY), 2);
    }

    static void setActionDelay(final int value) {
	PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.ACTION_DELAY), value);
    }

    public static long getActionSpeed() {
	return (PrefsManager.getActionDelay() + 1) * 5;
    }

    public static long getReplaySpeed() {
	return (PrefsManager.getActionDelay() + 1) * 10;
    }

    public static boolean oneMove() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ONE_MOVE), true);
    }

    static void setOneMove(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ONE_MOVE), value);
    }

    public static boolean enableAnimation() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), true);
    }

    static void setEnableAnimation(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), value);
    }

    public static boolean isKidsDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), true);
    }

    public static void setKidsDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), value);
    }

    public static boolean isEasyDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), true);
    }

    public static void setEasyDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), value);
    }

    public static boolean isMediumDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), true);
    }

    public static void setMediumDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), value);
    }

    public static boolean isHardDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), true);
    }

    public static void setHardDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), value);
    }

    public static boolean isDeadlyDifficultyEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), true);
    }

    public static void setDeadlyDifficultyEnabled(final boolean value) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), value);
    }

    public static AbstractDungeonObject getEditorDefaultFill() {
	return new Ground();
    }

    public static boolean getSoundsEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), true);
    }

    static void setSoundsEnabled(final boolean status) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), status);
    }

    public static boolean getMusicEnabled() {
	return PrefsManager.storeMgr.getBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), true);
    }

    static void setMusicEnabled(final boolean status) {
	PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), status);
    }

    public static JFrame getPrefFrame() {
	return PrefsManager.guiMgr.getPrefFrame();
    }

    public static void showPrefs() {
	PrefsManager.guiMgr.showPrefs();
    }

    private static String getPrefsDirPrefix() {
	final String osName = System.getProperty(Strings.untranslated(Untranslated.OS_NAME));
	if (osName.indexOf(Strings.untranslated(Untranslated.MACOS)) != -1) {
	    // Mac OS X
	    return System.getenv(Strings.untranslated(Untranslated.UNIX_HOME));
	} else if (osName.indexOf(Strings.untranslated(Untranslated.WINDOWS)) != -1) {
	    // Windows
	    return System.getenv(Strings.untranslated(Untranslated.WINDOWS_SUPPORT));
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(Strings.untranslated(Untranslated.UNIX_HOME));
	}
    }

    private static String getPrefsDirectory() {
	final String osName = System.getProperty(Strings.untranslated(Untranslated.OS_NAME));
	String base;
	if (osName.indexOf(Strings.untranslated(Untranslated.MACOS)) != -1) {
	    // Mac OS X
	    base = Strings.untranslated(Untranslated.MACOS_SUPPORT);
	} else if (osName.indexOf(Strings.untranslated(Untranslated.WINDOWS)) != -1) {
	    // Windows
	    base = Strings.EMPTY;
	} else {
	    // Other - assume UNIX-like
	    base = Strings.untranslated(Untranslated.UNIX_SUPPORT);
	}
	if (base != Strings.EMPTY) {
	    return base + File.pathSeparator + Strings.untranslated(Untranslated.COMPANY_SUBFOLDER) + File.pathSeparator
		    + Strings.untranslated(Untranslated.PROGRAM_NAME);
	} else {
	    return Strings.untranslated(Untranslated.COMPANY_SUBFOLDER) + File.pathSeparator
		    + Strings.untranslated(Untranslated.PROGRAM_NAME);
	}
    }

    private static String getPrefsFileExtension() {
	return Strings.fileExtension(FileExtension.PREFS);
    }

    private static String getPrefsFileName() {
	return Strings.untranslated(Untranslated.PREFS_FILE);
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
	    PrefsManager.storeMgr.setString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), Strings.EMPTY);
	    PrefsManager.storeMgr.setString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), Strings.EMPTY);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ONE_MOVE), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), true);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), true);
	    PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.ACTION_DELAY), 2);
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), false);
	    PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), PrefsManager.FALLBACK_LANGUAGE);
	    PrefsManager.storeMgr.setInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT),
		    PrefsManager.DEFAULT_EDITOR_LAYOUT.ordinal());
	    PrefsManager.storeMgr.setBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), true);
	}
    }
}
