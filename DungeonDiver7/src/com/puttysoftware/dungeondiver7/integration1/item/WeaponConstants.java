/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.item;

import com.puttysoftware.dungeondiver7.integration1.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.integration1.names.ZoneNames;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.LocalizedFile;

public class WeaponConstants {
    // Private Constructor
    private WeaponConstants() {
	// Do nothing
    }

    public static final int TYPE_COUNT = 6;
    private static final int[] HIT_SOUND_LOOKUP = { SoundConstants.AXE_HIT, SoundConstants.DAGGER_HIT,
	    SoundConstants.HAMMER_HIT, SoundConstants.STAFF_HIT, SoundConstants.SWORD_HIT, SoundConstants.WAND_HIT };

    public static synchronized String[] getWeaponChoices() {
	return LocaleLoader.loadAllStrings(LocalizedFile.WEAPON_TYPES, 6);
    }

    public static synchronized String getWeapon(final int index) {
	return LocaleLoader.loadString(LocalizedFile.WEAPON_TYPES, ZoneNames.getZoneNumber(index));
    }

    public static int getWeaponTypeHitSound(final int index) {
	return WeaponConstants.HIT_SOUND_LOOKUP[index];
    }
}
