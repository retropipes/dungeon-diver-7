/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.item;

import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class WeaponHit {
    private static final Sounds[] HIT_SOUND_LOOKUP = { Sounds.ATTACK_AXE, Sounds.ATTACK_KNIFE, Sounds.ATTACK_HAMMER,
	    Sounds.ATTACK_CLUB, Sounds.ATTACK_SWORD, Sounds.ATTACK_MACE };

    public static Sounds getWeaponTypeHitSound(final int index) {
	return WeaponHit.HIT_SOUND_LOOKUP[index];
    }

    // Private Constructor
    private WeaponHit() {
	// Do nothing
    }
}
