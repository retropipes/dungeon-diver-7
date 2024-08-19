/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.prefs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.creature.GameDifficulty;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.objects.Ground;
import org.retropipes.dungeondiver7.locale.EditorLayout;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.PrefKey;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public class Prefs {
    // Fields
    private static PrefsFile file;
    private static PrefsGUI gui;
    private final static int FALLBACK_LANGUAGE = 0;
    private final static EditorLayout DEFAULT_EDITOR_LAYOUT = EditorLayout.VERTICAL;
    private static final int BATTLE_SPEED = 1000;
    private static final int VIEWING_WINDOW_SIZE = 11;
    private static final GameDifficulty DEFAULT_DIFFICULTY = GameDifficulty.NORMAL;

    public static void activeLanguageChanged() {
	Prefs.gui.activeLanguageChanged();
    }

    public static boolean enableAnimation() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), true);
    }

    static int getActionDelay() {
	return Prefs.file.getInteger(Strings.prefKey(PrefKey.ACTION_DELAY), 2);
    }

    public static long getActionSpeed() {
	return (Prefs.getActionDelay() + 1) * 5;
    }

    public static int getBattleSpeed() {
	return Prefs.BATTLE_SPEED;
    }

    public static DungeonObject getEditorDefaultFill() {
	return new Ground();
    }

    public static EditorLayout getEditorLayout() {
	return EditorLayout.values()[Prefs.file.getInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT),
		Prefs.DEFAULT_EDITOR_LAYOUT.ordinal())];
    }

    public static boolean getEditorShowAllObjects() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), true);
    }

    public static GameDifficulty getGameDifficulty() {
	return GameDifficulty.values()[Prefs.file.getInteger(Strings.prefKey(PrefKey.GAME_DIFFICULTY),
		Prefs.DEFAULT_DIFFICULTY.ordinal())];
    }

    public static int getLanguageID() {
	return Prefs.file.getInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), Prefs.FALLBACK_LANGUAGE);
    }

    public static String getLastDirOpen() {
	return Prefs.file.getString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), Strings.EMPTY);
    }

    public static String getLastDirSave() {
	return Prefs.file.getString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), Strings.EMPTY);
    }

    public static boolean getMusicEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), true);
    }

    private static String getPrefsDirectory() {
	final var osName = System.getProperty(Strings.untranslated(Untranslated.OS_NAME));
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
	}
	return Strings.untranslated(Untranslated.COMPANY_SUBFOLDER) + File.pathSeparator
		+ Strings.untranslated(Untranslated.PROGRAM_NAME);
    }

    private static String getPrefsDirPrefix() {
	final var osName = System.getProperty(Strings.untranslated(Untranslated.OS_NAME));
	if (osName.indexOf(Strings.untranslated(Untranslated.MACOS)) != -1) {
	    // Mac OS X
	    return System.getenv(Strings.untranslated(Untranslated.UNIX_HOME));
	}
	if (osName.indexOf(Strings.untranslated(Untranslated.WINDOWS)) != -1) {
	    // Windows
	    return System.getenv(Strings.untranslated(Untranslated.WINDOWS_SUPPORT));
	}
	// Other - assume UNIX-like
	return System.getenv(Strings.untranslated(Untranslated.UNIX_HOME));
    }

    private static String getPrefsFile() {
	final var b = new StringBuilder();
	b.append(Prefs.getPrefsDirPrefix());
	b.append(Prefs.getPrefsDirectory());
	b.append(Prefs.getPrefsFileName());
	b.append(Prefs.getPrefsFileExtension());
	return b.toString();
    }

    private static String getPrefsFileExtension() {
	return Strings.fileExtension(FileExtension.PREFS);
    }

    private static String getPrefsFileName() {
	return Strings.untranslated(Untranslated.PREFS_FILE);
    }

    public static long getReplaySpeed() {
	return (Prefs.getActionDelay() + 1) * 10;
    }

    public static boolean getSoundsEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), true);
    }

    public static int getViewingWindowSize() {
	return Prefs.VIEWING_WINDOW_SIZE;
    }

    public static void init() {
	Prefs.file = new PrefsFile();
	Prefs.gui = new PrefsGUI();
    }

    public static boolean isDeadlyDifficultyEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), true);
    }

    public static boolean isEasyDifficultyEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), true);
    }

    public static boolean isHardDifficultyEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), true);
    }

    public static boolean isKidsDifficultyEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), true);
    }

    public static boolean isMediumDifficultyEnabled() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), true);
    }

    public static boolean oneMove() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ONE_MOVE), true);
    }

    public static void readPrefs() {
	try (var buf = new BufferedInputStream(new FileInputStream(Prefs.getPrefsFile()))) {
	    // Read new preferences
	    Prefs.file.loadStore(buf);
	} catch (final IOException io) {
	    // Populate store with defaults
	    Prefs.file.setString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), Strings.EMPTY);
	    Prefs.file.setString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), Strings.EMPTY);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ONE_MOVE), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), true);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), true);
	    Prefs.file.setInteger(Strings.prefKey(PrefKey.ACTION_DELAY), 2);
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), false);
	    Prefs.file.setInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), Prefs.FALLBACK_LANGUAGE);
	    Prefs.file.setInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT), Prefs.DEFAULT_EDITOR_LAYOUT.ordinal());
	    Prefs.file.setBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), true);
	}
    }

    static void setActionDelay(final int value) {
	Prefs.file.setInteger(Strings.prefKey(PrefKey.ACTION_DELAY), value);
    }

    static void setCheckUpdatesAtStartup(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), value);
    }

    public static void setClassicAccelerators(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), value);
    }

    public static void setDeadlyDifficultyEnabled(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), value);
    }

    public static void setEasyDifficultyEnabled(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), value);
    }

    public static void setEditorLayout(final EditorLayout value) {
	Prefs.file.setInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT), value.ordinal());
	DungeonDiver7.getStuffBag().getEditor().resetBorderPane();
    }

    public static void setEditorShowAllObjects(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), value);
	DungeonDiver7.getStuffBag().getEditor().resetBorderPane();
    }

    static void setEnableAnimation(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), value);
    }

    static void setGameDifficulty(final int value) {
	Prefs.file.setInteger(Strings.prefKey(PrefKey.GAME_DIFFICULTY), value);
    }

    public static void setHardDifficultyEnabled(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), value);
    }

    public static void setKidsDifficultyEnabled(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), value);
    }

    public static void setLanguageID(final int value) {
	final var oldValue = Prefs.getLanguageID();
	Prefs.file.setInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), value);
	if (oldValue != value) {
	    Strings.changeLanguage(Locale.getDefault());
	    DungeonConstants.activeLanguageChanged();
	    DungeonDiver7.getStuffBag().activeLanguageChanged();
	    Prefs.activeLanguageChanged();
	}
    }

    public static void setLastDirOpen(final String value) {
	Prefs.file.setString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), value);
    }

    public static void setLastDirSave(final String value) {
	Prefs.file.setString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), value);
    }

    public static void setMediumDifficultyEnabled(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), value);
    }

    static void setMusicEnabled(final boolean status) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), status);
    }

    static void setOneMove(final boolean value) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ONE_MOVE), value);
    }

    static void setSoundsEnabled(final boolean status) {
	Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), status);
    }

    public static boolean shouldCheckUpdatesAtStartup() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), true);
    }

    public static void showPrefs() {
	Prefs.gui.showPrefs();
    }

    public static boolean useClassicAccelerators() {
	return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), false);
    }

    public static void writePrefs() {
	try (var buf = new BufferedOutputStream(new FileOutputStream(Prefs.getPrefsFile()))) {
	    Prefs.file.saveStore(buf);
	} catch (final IOException io) {
	    // Ignore
	}
    }

    // Private constructor
    private Prefs() {
	// Do nothing
    }
}
