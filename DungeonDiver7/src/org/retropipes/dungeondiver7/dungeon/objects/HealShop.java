/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractShop;
import org.retropipes.dungeondiver7.shop.ShopType;

public class HealShop extends AbstractShop {
    // Constructors
    public HealShop() {
	super(ShopType.HEALER);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.HEAL_SHOP;
    }

    @Override
    public String getDescription() {
	return "Heal Shops restore health, for a fee.";
    }

    @Override
    public String getName() {
	return "Heal Shop";
    }

    @Override
    public String getPluralName() {
	return "Heal Shops";
    }
}
