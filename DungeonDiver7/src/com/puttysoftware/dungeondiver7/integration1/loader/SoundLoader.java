/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.loader;

import java.net.URL;

import com.puttysoftware.audio.wav.WAVPlayer;
import com.puttysoftware.dungeondiver7.manager.file.Extension;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;

public class SoundLoader {
    private static final String DEFAULT_LOAD_PATH = "/assets/sounds/";
    private static String LOAD_PATH = SoundLoader.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = SoundLoader.class;

    private static WAVPlayer getSound(final String filename) {
	final URL url = SoundLoader.LOAD_CLASS
		.getResource(SoundLoader.LOAD_PATH + filename.toLowerCase() + Extension.getSoundExtensionWithPeriod());
	return WAVPlayer.loadResource(url);
    }

    public static void playSound(final int soundID) {
	try {
	    if (PrefsManager.getSoundsEnabled()) {
		final String soundName = SoundConstants.getSoundName(soundID);
		final WAVPlayer snd = SoundLoader.getSound(soundName);
		snd.play();
	    }
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }
}