/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.item;

import org.retropipes.dungeondiver7.locale.Slot;

public class SlotUtils {
    public static Slot getArmorSlotForType(final int armorType) {
	if (armorType >= Slot.WEAPON.ordinal()) {
	    return Slot.values()[armorType + 1];
	}
	return Slot.values()[armorType];
    }
}
