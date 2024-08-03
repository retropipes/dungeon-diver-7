/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.gameobject;

public enum ShopType {
    ARMOR(11), BANK(12), HEALER(13), ITEMS(14), SPELLS(15), SURGE(16), WEAPONS(17);

    final int shopID;

    private ShopType(final int sid) {
	this.shopID = sid;
    }

    public final int getShopID() {
	return this.shopID;
    }

    public static final ShopType getFromShopID(final int sid) {
	switch (sid) {
	case 11:
	    return ARMOR;
	case 12:
	    return BANK;
	case 13:
	    return HEALER;
	case 14:
	    return ITEMS;
	case 15:
	    return SPELLS;
	case 16:
	    return SURGE;
	case 17:
	    return WEAPONS;
	default:
	    return null;
	}
    }
}