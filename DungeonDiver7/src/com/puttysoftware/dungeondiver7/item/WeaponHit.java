/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import com.puttysoftware.dungeondiver7.loader.SoundConstants;

public class WeaponHit {
    // Private Constructor
    private WeaponHit() {
	// Do nothing
    }

    private static final int[] HIT_SOUND_LOOKUP = { SoundConstants.AXE_HIT, SoundConstants.DAGGER_HIT,
	    SoundConstants.HAMMER_HIT, SoundConstants.STAFF_HIT, SoundConstants.SWORD_HIT, SoundConstants.WAND_HIT };

    public static int getWeaponTypeHitSound(final int index) {
	return WeaponHit.HIT_SOUND_LOOKUP[index];
    }
}
