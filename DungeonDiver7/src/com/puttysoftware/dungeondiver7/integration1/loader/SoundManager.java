/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.loader;

import java.net.URL;

import com.puttysoftware.audio.wav.WAVPlayer;
import com.puttysoftware.dungeondiver7.integration1.prefs.PreferencesManager;
import com.puttysoftware.dungeondiver7.manager.file.Extension;

public class SoundManager {
    private static final String DEFAULT_LOAD_PATH = "/assets/sounds/";
    private static String LOAD_PATH = SoundManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = SoundManager.class;

    private static WAVPlayer getSound(final String filename) {
	final URL url = SoundManager.LOAD_CLASS
		.getResource(SoundManager.LOAD_PATH + filename.toLowerCase() + Extension.getSoundExtensionWithPeriod());
	return WAVPlayer.loadResource(url);
    }

    public static void playSound(final int soundID) {
	try {
	    if (PreferencesManager.getSoundsEnabled()) {
		final String soundName = SoundConstants.getSoundName(soundID);
		final WAVPlayer snd = SoundManager.getSound(soundName);
		snd.play();
	    }
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }
}