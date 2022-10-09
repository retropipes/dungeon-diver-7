/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.audio.wav.WAVPlayer;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.prefs.Prefs;

public class SoundLoader {
    private static Class<?> LOAD_CLASS = SoundLoader.class;

    private SoundLoader() {
	// Do nothing
    }

    private static WAVPlayer getSound(final int soundID) {
	final var filename = SoundConstants.getSoundName(soundID);
	final var url = SoundLoader.LOAD_CLASS.getResource(Strings.untranslated(Untranslated.SOUND_LOAD_PATH)
		+ filename.toLowerCase() + Strings.fileExtension(FileExtension.SOUND));
	return WAVPlayer.loadResource(url);
    }

    public static void playSound(final int soundID) {
	if (Prefs.getSoundsEnabled()) {
	    SoundLoader.getSound(soundID).play();
	}
    }
}