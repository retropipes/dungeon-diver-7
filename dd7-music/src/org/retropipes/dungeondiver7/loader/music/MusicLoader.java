/*  SharedX: An RPG
 Copyright (C) 2011-2012 Eric Ahnell

 Any questions should be directed to the author via email at: realmzxfamily@worldwizard.net
 */
package org.retropipes.dungeondiver7.loader.music;

import java.nio.BufferUnderflowException;

import org.retropipes.diane.asset.ogg.DianeOggPlayer;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Music;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public class MusicLoader {
	// Fields
	private static DianeOggPlayer CURRENT_MUSIC;
	private static Class<?> LOAD_CLASS = MusicLoader.class;

	private static DianeOggPlayer getMusic(final String filename) {
		final var oggFile = MusicLoader.LOAD_CLASS.getResource(Strings.untranslated(Untranslated.MUSIC_LOAD_PATH)
				+ filename + Strings.fileExtension(FileExtension.MUSIC));
		return DianeOggPlayer.loadLoopedResource(oggFile);
	}

	public static boolean isMusicPlaying() {
		if (MusicLoader.CURRENT_MUSIC != null) {
			return MusicLoader.CURRENT_MUSIC.isAlive();
		}
		return false;
	}

	public static void playMusic(final Music musicID) {
		MusicLoader.CURRENT_MUSIC = MusicLoader.getMusic(MusicCatalogLoader.getMusicFilename(musicID));
		if (MusicLoader.CURRENT_MUSIC != null) {
			// Play the music
			MusicLoader.CURRENT_MUSIC.play();
		}
	}

	public static void stopMusic() {
		if (MusicLoader.CURRENT_MUSIC != null) {
			// Stop the music
			try {
				DianeOggPlayer.stopPlaying();
			} catch (final BufferUnderflowException bue) {
				// Ignore
			}
		}
	}

	// Constructors
	private MusicLoader() {
		// Do nothing
	}
}