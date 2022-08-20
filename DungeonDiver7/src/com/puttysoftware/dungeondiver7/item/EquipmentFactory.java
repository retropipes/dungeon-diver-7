/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.shop.Shop;

public class EquipmentFactory {
    // Private constructor
    private EquipmentFactory() {
	// Do nothing
    }

    // Methods
    public static Equipment createWeapon(final int material, final int weaponType) {
	return new Equipment(Strings.weaponName(material, weaponType), Shop.getEquipmentCost(material), material + 1,
		material + 1, EquipmentSlotConstants.SLOT_WEAPON, material,
		WeaponHit.getWeaponTypeHitSound(weaponType));
    }

    public static Equipment createArmor(final int material, final int armorType) {
	return new Equipment(Strings.armorName(material, armorType), Shop.getEquipmentCost(material), material + 1,
		material + 1, EquipmentSlotConstants.getArmorSlotForType(armorType), material, -1);
    }
}
