/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import com.puttysoftware.dungeondiver7.loader.Sounds;

public class WeaponHit {
    // Private Constructor
    private WeaponHit() {
        // Do nothing
    }

    private static final Sounds[] HIT_SOUND_LOOKUP = { Sounds.AXE_HIT, Sounds.DAGGER_HIT,
            Sounds.HAMMER_HIT, Sounds.STAFF_HIT, Sounds.SWORD_HIT, Sounds.WAND_HIT };

    public static Sounds getWeaponTypeHitSound(final int index) {
        return WeaponHit.HIT_SOUND_LOOKUP[index];
    }
}
