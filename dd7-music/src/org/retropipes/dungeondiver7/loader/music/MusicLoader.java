/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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