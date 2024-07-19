/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractShop;
import org.retropipes.dungeondiver7.loader.ObjectImageConstants;
import org.retropipes.dungeondiver7.shop.ShopType;

public class WeaponsShop extends AbstractShop {
	// Constructors
	public WeaponsShop() {
		super(ShopType.WEAPONS);
	}

	@Override
	public int getBaseID() {
		return ObjectImageConstants.WEAPONS_SHOP;
	}

	@Override
	public String getDescription() {
		return "Weapons Shops sell weapons used to fight monsters.";
	}

	@Override
	public String getName() {
		return "Weapons Shop";
	}

	@Override
	public String getPluralName() {
		return "Weapons Shops";
	}
}
