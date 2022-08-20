/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.old.LocalizedFile;
import com.puttysoftware.dungeondiver7.names.Zones;

public class ArmorConstants {
    // Private Constructor
    private ArmorConstants() {
	// Do nothing
    }

    public static final int TYPE_COUNT = 6;

    public static synchronized String[] getArmorChoices() {
	return LocaleLoader.loadAllStrings(LocalizedFile.ARMOR_TYPES, 6);
    }

    public static synchronized String getArmor(final int index) {
	return LocaleLoader.loadString(LocalizedFile.ARMOR_TYPES, Zones.getZoneNumber(index));
    }
}
