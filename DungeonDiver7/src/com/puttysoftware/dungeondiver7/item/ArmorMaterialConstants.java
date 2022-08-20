/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.old.LocalizedFile;
import com.puttysoftware.dungeondiver7.names.Zones;

class ArmorMaterialConstants {
    // Private Constructor
    private ArmorMaterialConstants() {
	// Do nothing
    }

    public static synchronized String getArmorMaterial(final int index) {
	return LocaleLoader.loadString(LocalizedFile.ARMOR, Zones.getZoneNumber(index));
    }
}
