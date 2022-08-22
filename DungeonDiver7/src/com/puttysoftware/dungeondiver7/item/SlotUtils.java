/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import com.puttysoftware.dungeondiver7.locale.Slot;

public class SlotUtils {
    public static Slot getArmorSlotForType(final int armorType) {
	if (armorType >= Slot.WEAPON.ordinal()) {
	    return Slot.values()[armorType + 1];
	}
	return Slot.values()[armorType];
    }
}
