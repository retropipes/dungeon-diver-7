/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractShop;
import org.retropipes.dungeondiver7.gameobject.ShopType;

public class Regenerator extends AbstractShop {
    // Constructors
    public Regenerator() {
	super(ShopType.SURGE);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.REGENERATOR;
    }
}
