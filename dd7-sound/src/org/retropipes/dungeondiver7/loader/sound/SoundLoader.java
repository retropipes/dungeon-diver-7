/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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