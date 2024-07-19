/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractShop;
import org.retropipes.dungeondiver7.loader.ObjectImageConstants;
import org.retropipes.dungeondiver7.shop.ShopType;

public class ArmorShop extends AbstractShop {
	// Constructors
	public ArmorShop() {
		super(ShopType.ARMOR);
	}

	@Override
	public int getBaseID() {
		return ObjectImageConstants.ARMOR_SHOP;
	}

	@Override
	public String getDescription() {
		return "Armor Shops sell protective armor.";
	}

	@Override
	public String getName() {
		return "Armor Shop";
	}

	@Override
	public String getPluralName() {
		return "Armor Shops";
	}
}
