/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.item;

import com.puttysoftware.dungeondiver7.integration1.names.ZoneNames;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.LocalizedFile;

class WeaponMaterialConstants {
    // Private Constructor
    private WeaponMaterialConstants() {
	// Do nothing
    }

    public static synchronized String getWeaponMaterial(final int index) {
	return LocaleLoader.loadString(LocalizedFile.WEAPONS, ZoneNames.getZoneNumber(index));
    }
}
