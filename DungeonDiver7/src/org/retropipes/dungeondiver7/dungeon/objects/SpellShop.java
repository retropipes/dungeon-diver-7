/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractShop;
import org.retropipes.dungeondiver7.shop.ShopType;

public class SpellShop extends AbstractShop {
	// Constructors
	public SpellShop() {
		super(ShopType.SPELLS);
	}

	@Override
	public int getBaseID() {
		return ObjectImageConstants.SPELL_SHOP;
	}

	@Override
	public String getDescription() {
		return "Spell Shops teach spells, for a fee.";
	}

	@Override
	public String getName() {
		return "Spell Shop";
	}

	@Override
	public String getPluralName() {
		return "Spell Shops";
	}
}