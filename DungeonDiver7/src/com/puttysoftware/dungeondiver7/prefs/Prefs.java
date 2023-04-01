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

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.locale.EditorLayout;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.PrefKey;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

public class Prefs {
    // Fields
    private static PrefsFile file;
    private static PrefsGUI gui;
    private final static int FALLBACK_LANGUAGE = 0;
    private final static EditorLayout DEFAULT_EDITOR_LAYOUT = EditorLayout.VERTICAL;
    private static final int BATTLE_SPEED = 1000;
    private static final int VIEWING_WINDOW_SIZE = 11;
    public static final int DIFFICULTY_VERY_EASY = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_NORMAL = 2;
    public static final int DIFFICULTY_HARD = 3;
    public static final int DIFFICULTY_VERY_HARD = 4;
    private static final int DEFAULT_DIFFICULTY = Prefs.DIFFICULTY_NORMAL;

    // Private constructor
    private Prefs() {
        // Do nothing
    }

    // Methods
    public static void init() {
        Prefs.file = new PrefsFile();
        Prefs.gui = new PrefsGUI();
    }

    public static int getBattleSpeed() {
        return Prefs.BATTLE_SPEED;
    }

    public static int getViewingWindowSize() {
        return Prefs.VIEWING_WINDOW_SIZE;
    }

    public static int getGameDifficulty() {
        return Prefs.file.getInteger(Strings.prefKey(PrefKey.GAME_DIFFICULTY), Prefs.DEFAULT_DIFFICULTY);
    }

    static void setGameDifficulty(final int value) {
        Prefs.file.setInteger(Strings.prefKey(PrefKey.GAME_DIFFICULTY), value);
    }

    public static void activeLanguageChanged() {
        Prefs.gui.activeLanguageChanged();
    }

    public static EditorLayout getEditorLayout() {
        return EditorLayout.values()[Prefs.file.getInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT),
                Prefs.DEFAULT_EDITOR_LAYOUT.ordinal())];
    }

    public static void setEditorLayout(final EditorLayout value) {
        Prefs.file.setInteger(Strings.prefKey(PrefKey.EDITOR_LAYOUT), value.ordinal());
        DungeonDiver7.getStuffBag().getEditor().resetBorderPane();
    }

    public static boolean getEditorShowAllObjects() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), true);
    }

    public static void setEditorShowAllObjects(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.EDITOR_SHOW_ALL_OBJECTS), value);
        DungeonDiver7.getStuffBag().getEditor().resetBorderPane();
    }

    public static int getLanguageID() {
        return Prefs.file.getInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), Prefs.FALLBACK_LANGUAGE);
    }

    public static void setLanguageID(final int value) {
        final var oldValue = Prefs.getLanguageID();
        Prefs.file.setInteger(Strings.prefKey(PrefKey.ACTIVE_LANGUAGE), value);
        if (oldValue != value) {
            Strings.changeLanguage(Locale.getDefault());
        }
    }

    public static boolean useClassicAccelerators() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), false);
    }

    public static void setClassicAccelerators(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ACCELERATOR_MODEL), value);
    }

    public static String getLastDirOpen() {
        return Prefs.file.getString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), Strings.EMPTY);
    }

    public static void setLastDirOpen(final String value) {
        Prefs.file.setString(Strings.prefKey(PrefKey.LAST_FOLDER_OPEN), value);
    }

    public static String getLastDirSave() {
        return Prefs.file.getString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), Strings.EMPTY);
    }

    public static void setLastDirSave(final String value) {
        Prefs.file.setString(Strings.prefKey(PrefKey.LAST_FOLDER_SAVE), value);
    }

    public static boolean shouldCheckUpdatesAtStartup() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), true);
    }

    static void setCheckUpdatesAtStartup(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.UPDATES_STARTUP), value);
    }

    static int getActionDelay() {
        return Prefs.file.getInteger(Strings.prefKey(PrefKey.ACTION_DELAY), 2);
    }

    static void setActionDelay(final int value) {
        Prefs.file.setInteger(Strings.prefKey(PrefKey.ACTION_DELAY), value);
    }

    public static long getActionSpeed() {
        return (Prefs.getActionDelay() + 1) * 5;
    }

    public static long getReplaySpeed() {
        return (Prefs.getActionDelay() + 1) * 10;
    }

    public static boolean oneMove() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ONE_MOVE), true);
    }

    static void setOneMove(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ONE_MOVE), value);
    }

    public static boolean enableAnimation() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), true);
    }

    static void setEnableAnimation(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_ANIMATION), value);
    }

    public static boolean isKidsDifficultyEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), true);
    }

    public static void setKidsDifficultyEnabled(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_KIDS), value);
    }

    public static boolean isEasyDifficultyEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), true);
    }

    public static void setEasyDifficultyEnabled(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_EASY), value);
    }

    public static boolean isMediumDifficultyEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), true);
    }

    public static void setMediumDifficultyEnabled(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_MEDIUM), value);
    }

    public static boolean isHardDifficultyEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), true);
    }

    public static void setHardDifficultyEnabled(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_HARD), value);
    }

    public static boolean isDeadlyDifficultyEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), true);
    }

    public static void setDeadlyDifficultyEnabled(final boolean value) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_DIFFICULTY_DEADLY), value);
    }

    public static AbstractDungeonObject getEditorDefaultFill() {
        return new Ground();
    }

    public static boolean getSoundsEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), true);
    }

    static void setSoundsEnabled(final boolean status) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_SOUNDS), status);
    }

    public static boolean getMusicEnabled() {
        return Prefs.file.getBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), true);
    }

    static void setMusicEnabled(final boolean status) {
        Prefs.file.setBoolean(Strings.prefKey(PrefKey.ENABLE_MUSIC), status);
    }

    public static void showPrefs() {
        Prefs.gui.showPrefs();
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

    private static String getPrefsFileExtension() {
        return Strings.fileExtension(FileExtension.PREFS);
    }

    private static String getPrefsFileName() {
        return Strings.untranslated(Untranslated.PREFS_FILE);
    }

    private static String getPrefsFile() {
        final var b = new StringBuilder();
        b.append(Prefs.getPrefsDirPrefix());
        b.append(Prefs.getPrefsDirectory());
        b.append(Prefs.getPrefsFileName());
        b.append(Prefs.getPrefsFileExtension());
        return b.toString();
    }

    public static void writePrefs() {
        try (var buf = new BufferedOutputStream(new FileOutputStream(Prefs.getPrefsFile()))) {
            Prefs.file.saveStore(buf);
        } catch (final IOException io) {
            // Ignore
        }
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
}
