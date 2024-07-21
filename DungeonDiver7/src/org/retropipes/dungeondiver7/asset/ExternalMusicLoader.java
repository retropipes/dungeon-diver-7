/*  SharedX: An RPG
 Copyright (C) 2011-2012 Eric Ahnell

 Any questions should be directed to the author via email at: realmzxfamily@worldwizard.net
 */
package org.retropipes.dungeondiver7.asset;

import java.io.File;

import org.retropipes.diane.asset.ogg.DianeOggPlayer;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;

public class ExternalMusicLoader {
	// Fields
	private static String EXTERNAL_LOAD_PATH = null;
	private static DianeOggPlayer CURRENT_EXTERNAL_MUSIC;
	private static ExternalMusic gameExternalMusic;

	public static void deleteExternalMusicFile() {
		final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
		final var file = new File(a.getDungeonTempMusicFolder() + a.getMusicFilename());
		file.delete();
	}

	public static void dungeonChanged() {
		ExternalMusicLoader.EXTERNAL_LOAD_PATH = null;
	}

	public static ExternalMusic getExternalMusic() {
		if (ExternalMusicLoader.gameExternalMusic == null) {
			ExternalMusicLoader.loadExternalMusic();
		}
		return ExternalMusicLoader.gameExternalMusic;
	}

	private static DianeOggPlayer getExternalMusic(final String filename) {
		if (ExternalMusicLoader.EXTERNAL_LOAD_PATH == null) {
			ExternalMusicLoader.EXTERNAL_LOAD_PATH = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
					.getDungeonTempMusicFolder();
		}
		final var mmod = DianeOggPlayer.loadLoopedFile(ExternalMusicLoader.EXTERNAL_LOAD_PATH + filename);
		ExternalMusicLoader.CURRENT_EXTERNAL_MUSIC = mmod;
		return mmod;
	}

	public static boolean isExternalMusicPlaying() {
		if (ExternalMusicLoader.CURRENT_EXTERNAL_MUSIC != null && ExternalMusicLoader.CURRENT_EXTERNAL_MUSIC.isPlaying()) {
			return true;
		}
		return false;
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

	public static void loadPlayExternalMusic(final String filename) {
		final var mmod = ExternalMusicLoader.getExternalMusic(filename);
		if (mmod != null) {
			ExternalMusicLoader.CURRENT_EXTERNAL_MUSIC = mmod;
			mmod.play();
		}
	}

	public static void playExternalMusic() {
		final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
		final var mmod = ExternalMusicLoader.getExternalMusic(a.getMusicFilename());
		if (mmod != null) {
			mmod.play();
		}
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
		final var filename = ExternalMusicLoader.gameExternalMusic.getName();
		final var filepath = ExternalMusicLoader.gameExternalMusic.getPath();
		final var esst = new ExternalMusicSaveTask(filepath, filename);
		esst.start();
	}

	public static void setExternalMusic(final ExternalMusic newExternalMusic) {
		ExternalMusicLoader.gameExternalMusic = newExternalMusic;
	}

	public static void stopExternalMusic() {
		if (ExternalMusicLoader.isExternalMusicPlaying()) {
			DianeOggPlayer.stopPlaying();
		}
	}

	// Constructors
	private ExternalMusicLoader() {
		// Do nothing
	}
}