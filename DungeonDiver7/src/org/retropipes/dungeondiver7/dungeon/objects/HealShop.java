/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractShop;
import org.retropipes.dungeondiver7.gameobject.ShopType;

public class HealShop extends AbstractShop {
    // Constructors
    public HealShop() {
	super(ShopType.HEALER);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.HEAL_SHOP;
    }
}
