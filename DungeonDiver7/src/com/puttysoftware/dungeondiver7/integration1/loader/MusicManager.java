/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.loader;

import java.net.URL;
import java.nio.BufferUnderflowException;

import com.puttysoftware.audio.ogg.OggPlayer;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.integration1.manager.file.Extension;

public class MusicManager {
    private static final String DEFAULT_LOAD_PATH = "/assets/music/";
    private static String LOAD_PATH = MusicManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = MusicManager.class;
    private static OggPlayer CURRENT_MUSIC;

    private static OggPlayer getMusic(final String filename) {
	final URL modFile = MusicManager.LOAD_CLASS
		.getResource(MusicManager.LOAD_PATH + filename + Extension.getMusicExtensionWithPeriod());
	return OggPlayer.loadLoopedResource(modFile);
    }

    public static void playMusic(final int musicID) {
	MusicManager.CURRENT_MUSIC = MusicManager.getMusic(MusicConstants.getMusicName(musicID));
	if (MusicManager.CURRENT_MUSIC != null) {
	    // Play the music
	    MusicManager.CURRENT_MUSIC.play();
	}
    }

    public static void stopMusic() {
	if (MusicManager.CURRENT_MUSIC != null) {
	    // Stop the music
	    try {
		OggPlayer.stopPlaying();
	    } catch (final BufferUnderflowException bue) {
		// Ignore
	    } catch (final Throwable t) {
		DungeonDiver7.getErrorLogger().logError(t);
	    }
	}
    }

    public static boolean isMusicPlaying() {
	if (MusicManager.CURRENT_MUSIC != null) {
	    return MusicManager.CURRENT_MUSIC.isAlive();
	}
	return false;
    }
}