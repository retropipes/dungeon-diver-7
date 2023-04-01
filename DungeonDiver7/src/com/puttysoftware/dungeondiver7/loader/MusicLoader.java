/*  SharedX: An RPG
 Copyright (C) 2011-2012 Eric Ahnell

 Any questions should be directed to the author via email at: realmzxfamily@worldwizard.net
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.File;
import java.nio.BufferUnderflowException;

import com.puttysoftware.audio.ogg.OggPlayer;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.editor.ExternalMusic;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Music;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

public class MusicLoader {
    // Fields
    private static String EXTERNAL_LOAD_PATH = null;
    private static OggPlayer CURRENT_MUSIC;
    private static OggPlayer CURRENT_EXTERNAL_MUSIC;
    private static ExternalMusic gameExternalMusic;
    private static Class<?> LOAD_CLASS = MusicLoader.class;

    // Constructors
    private MusicLoader() {
        // Do nothing
    }

    private static OggPlayer getMusic(final String filename) {
        final var modFile = MusicLoader.LOAD_CLASS.getResource(Strings.untranslated(Untranslated.MUSIC_LOAD_PATH)
                + filename + Strings.fileExtension(FileExtension.MUSIC));
        return OggPlayer.loadLoopedResource(modFile);
    }

    public static void playMusic(final Music musicID) {
        MusicLoader.CURRENT_MUSIC = MusicLoader.getMusic(Strings.music(musicID));
        if (MusicLoader.CURRENT_MUSIC != null) {
            // Play the music
            MusicLoader.CURRENT_MUSIC.play();
        }
    }

    public static void stopMusic() {
        if (MusicLoader.CURRENT_MUSIC != null) {
            // Stop the music
            try {
                OggPlayer.stopPlaying();
            } catch (final BufferUnderflowException bue) {
                // Ignore
            } catch (final Throwable t) {
                DungeonDiver7.logError(t);
            }
        }
    }

    public static boolean isMusicPlaying() {
        if (MusicLoader.CURRENT_MUSIC != null) {
            return MusicLoader.CURRENT_MUSIC.isAlive();
        }
        return false;
    }

    private static OggPlayer getExternalMusic(final String filename) {
        if (MusicLoader.EXTERNAL_LOAD_PATH == null) {
            MusicLoader.EXTERNAL_LOAD_PATH = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
                    .getDungeonTempMusicFolder();
        }
        final var mmod = OggPlayer.loadLoopedFile(MusicLoader.EXTERNAL_LOAD_PATH + filename);
        MusicLoader.CURRENT_EXTERNAL_MUSIC = mmod;
        return mmod;
    }

    public static boolean isExternalMusicPlaying() {
        if (MusicLoader.CURRENT_EXTERNAL_MUSIC != null && MusicLoader.CURRENT_EXTERNAL_MUSIC.isPlaying()) {
            return true;
        }
        return false;
    }

    public static void playExternalMusic() {
        final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
        final var mmod = MusicLoader.getExternalMusic(a.getMusicFilename());
        if (mmod != null) {
            mmod.play();
        }
    }

    public static void loadPlayExternalMusic(final String filename) {
        final var mmod = MusicLoader.getExternalMusic(filename);
        if (mmod != null) {
            MusicLoader.CURRENT_EXTERNAL_MUSIC = mmod;
            mmod.play();
        }
    }

    public static void stopExternalMusic() {
        if (MusicLoader.isExternalMusicPlaying()) {
            OggPlayer.stopPlaying();
        }
    }

    public static void dungeonChanged() {
        MusicLoader.EXTERNAL_LOAD_PATH = null;
    }

    public static ExternalMusic getExternalMusic() {
        if (MusicLoader.gameExternalMusic == null) {
            MusicLoader.loadExternalMusic();
        }
        return MusicLoader.gameExternalMusic;
    }

    public static void setExternalMusic(final ExternalMusic newExternalMusic) {
        MusicLoader.gameExternalMusic = newExternalMusic;
    }

    public static void loadExternalMusic() {
        final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
        final var ellt = new ExternalMusicLoadTask(a.getDungeonTempMusicFolder() + a.getMusicFilename());
        ellt.start();
        // Wait
        if (ellt.isAlive()) {
            var waiting = true;
            while (waiting) {
                try {
                    ellt.join();
                    waiting = false;
                } catch (final InterruptedException ie) {
                    // Ignore
                }
            }
        }
    }

    public static void deleteExternalMusicFile() {
        final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
        final var file = new File(a.getDungeonTempMusicFolder() + a.getMusicFilename());
        file.delete();
    }

    public static void saveExternalMusic() {
        // Write external music
        final var extMusicDir = new File(
                DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getDungeonTempMusicFolder());
        if (!extMusicDir.exists()) {
            final var res = extMusicDir.mkdirs();
            if (!res) {
                CommonDialogs.showErrorDialog("Save External Music Failed!", "External Music Editor");
                return;
            }
        }
        final var filename = MusicLoader.gameExternalMusic.getName();
        final var filepath = MusicLoader.gameExternalMusic.getPath();
        final var esst = new ExternalMusicSaveTask(filepath, filename);
        esst.start();
    }
}