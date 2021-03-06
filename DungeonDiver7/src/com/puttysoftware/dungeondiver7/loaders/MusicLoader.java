/*  SharedX: An RPG
 Copyright (C) 2011-2012 Eric Ahnell

 Any questions should be directed to the author via email at: realmzxfamily@worldwizard.net
 */
package com.puttysoftware.dungeondiver7.loaders;

import java.io.File;

import com.puttysoftware.audio.ogg.OggPlayer;
import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.editor.ExternalMusic;

public class MusicLoader {
    // Fields
    private static String EXTERNAL_LOAD_PATH = null;
    private static OggPlayer CURRENT_EXTERNAL_MUSIC;
    private static ExternalMusic gameExternalMusic;

    // Constructors
    private MusicLoader() {
	// Do nothing
    }

    private static OggPlayer getMusic(final String filename) {
	try {
	    if (MusicLoader.EXTERNAL_LOAD_PATH == null) {
		MusicLoader.EXTERNAL_LOAD_PATH = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
			.getDungeonTempMusicFolder();
	    }
	    final OggPlayer mmod = OggPlayer.loadLoopedFile(MusicLoader.EXTERNAL_LOAD_PATH + filename);
	    MusicLoader.CURRENT_EXTERNAL_MUSIC = mmod;
	    return mmod;
	} catch (final NullPointerException np) {
	    return null;
	}
    }

    public static boolean isMusicPlaying() {
	if (MusicLoader.CURRENT_EXTERNAL_MUSIC != null) {
	    if (MusicLoader.CURRENT_EXTERNAL_MUSIC.isPlaying()) {
		return true;
	    }
	}
	return false;
    }

    public static void playMusic() {
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	final OggPlayer mmod = MusicLoader.getMusic(a.getMusicFilename());
	if (mmod != null) {
	    mmod.play();
	}
    }

    public static void loadPlayMusic(final String filename) {
	final OggPlayer mmod = MusicLoader.getMusic(filename);
	if (mmod != null) {
	    MusicLoader.CURRENT_EXTERNAL_MUSIC = mmod;
	    mmod.play();
	}
    }

    public static void stopMusic() {
	if (MusicLoader.isMusicPlaying()) {
	    OggPlayer.stopPlaying();
	}
    }

    public static void dungeonChanged() {
	MusicLoader.EXTERNAL_LOAD_PATH = null;
    }

    // Methods
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
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	final ExternalMusicLoadTask ellt = new ExternalMusicLoadTask(
		a.getDungeonTempMusicFolder() + a.getMusicFilename());
	ellt.start();
	// Wait
	if (ellt.isAlive()) {
	    boolean waiting = true;
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
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	final File file = new File(a.getDungeonTempMusicFolder() + a.getMusicFilename());
	file.delete();
    }

    public static void saveExternalMusic() {
	// Write external music
	final File extMusicDir = new File(
		DungeonDiver7.getApplication().getDungeonManager().getDungeon().getDungeonTempMusicFolder());
	if (!extMusicDir.exists()) {
	    final boolean res = extMusicDir.mkdirs();
	    if (!res) {
		CommonDialogs.showErrorDialog("Save External Music Failed!", "External Music Editor");
		return;
	    }
	}
	final String filename = MusicLoader.gameExternalMusic.getName();
	final String filepath = MusicLoader.gameExternalMusic.getPath();
	final ExternalMusicSaveTask esst = new ExternalMusicSaveTask(filepath, filename);
	esst.start();
    }

    public static String getExtension(final File f) {
	String ext = null;
	final String s = f.getName();
	final int i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i + 1).toLowerCase();
	}
	return ext;
    }
}