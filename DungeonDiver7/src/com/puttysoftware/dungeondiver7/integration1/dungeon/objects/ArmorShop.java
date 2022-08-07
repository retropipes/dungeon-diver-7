/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractShop;
import com.puttysoftware.dungeondiver7.integration1.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.integration1.shop.ShopType;

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
    public String getName() {
	return "Armor Shop";
    }

    @Override
    public String getPluralName() {
	return "Armor Shops";
    }

    @Override
    public String getDescription() {
	return "Armor Shops sell protective armor.";
    }
}
