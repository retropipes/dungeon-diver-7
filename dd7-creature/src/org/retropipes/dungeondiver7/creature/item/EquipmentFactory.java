/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.item;

import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Slot;
import org.retropipes.dungeondiver7.locale.Strings;

public class EquipmentFactory {
    public static Equipment createArmor(final int material, final int armorType) {
	return new Equipment(Strings.armorName(material, armorType), ItemPrices.getEquipmentCost(material),
		material + 1, material + 1, SlotUtils.getArmorSlotForType(armorType), material, Sounds._NONE);
    }

    public static Equipment createWeapon(final int material, final int weaponType) {
	return new Equipment(Strings.weaponName(material, weaponType), ItemPrices.getEquipmentCost(material),
		material + 1, material + 1, Slot.WEAPON, material, WeaponHit.getWeaponTypeHitSound(weaponType));
    }

    // Private constructor
    private EquipmentFactory() {
	// Do nothing
    }
}
