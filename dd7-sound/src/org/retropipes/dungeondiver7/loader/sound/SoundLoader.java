/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.loader.sound;

import org.retropipes.diane.asset.sound.DianeSoundPlayer;

public class SoundLoader {
    public static void playSound(final Sounds soundID) {
	DianeSoundPlayer.play(soundID);
    }

    private SoundLoader() {
	// Do nothing
    }
}