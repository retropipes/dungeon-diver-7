/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import org.retropipes.diane.asset.sound.DianeSoundPlayer;

import com.puttysoftware.dungeondiver7.prefs.Prefs;

public class SoundLoader {
	public static void playSound(final Sounds soundID) {
		if (Prefs.getSoundsEnabled()) {
			DianeSoundPlayer.play(soundID);
		}
	}

	private SoundLoader() {
		// Do nothing
	}
}