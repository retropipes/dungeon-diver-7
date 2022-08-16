/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import java.net.URL;

import com.puttysoftware.audio.wav.WAVPlayer;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;

public class SoundLoader {
    private static final String DEFAULT_LOAD_PATH = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_SOUND_PATH);
    private static String LOAD_PATH = SoundLoader.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = SoundLoader.class;

    private SoundLoader() {
	// Do nothing
    }

    private static WAVPlayer getSound(final int soundID) {
	try {
	    final String filename = SoundConstants.getSoundName(soundID);
	    final URL url = SoundLoader.LOAD_CLASS
		    .getResource(SoundLoader.LOAD_PATH + filename.toLowerCase() + LocaleLoader.loadString(
			    LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_SOUND_EXTENSION));
	    return WAVPlayer.loadResource(url);
	} catch (final NullPointerException np) {
	    return null;
	}
    }

    public static void playSound(final int soundID) {
	if (PrefsManager.getSoundsEnabled()) {
	    SoundLoader.getSound(soundID).play();
	}
    }
}