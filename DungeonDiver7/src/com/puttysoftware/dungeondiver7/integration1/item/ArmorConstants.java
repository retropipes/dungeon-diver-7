/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.item;

import com.puttysoftware.dungeondiver7.integration1.names.ZoneNames;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.LocalizedFile;

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
	return LocaleLoader.loadString(LocalizedFile.ARMOR_TYPES, ZoneNames.getZoneNumber(index));
    }
}
