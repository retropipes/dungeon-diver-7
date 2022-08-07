/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.DungeonConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.shop.Shop;
import com.puttysoftware.dungeondiver7.shop.ShopType;

public abstract class AbstractShop extends AbstractGameObject {
    // Fields
    private final ShopType shopType;

    // Constructors
    public AbstractShop(final ShopType newShopType) {
	super(false, false);
	this.shopType = newShopType;
    }

    // Methods
    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_SHOP);
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	SoundLoader.playSound(SoundConstants.WALK);
    }

    @Override
    public void interactAction() {
	final Shop shop = Integration1.getApplication().getShopByType(this.shopType);
	if (shop != null) {
	    shop.showShop();
	}
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_OBJECT;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractGameObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
