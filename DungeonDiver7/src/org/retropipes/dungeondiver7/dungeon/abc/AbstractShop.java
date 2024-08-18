/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.gameobject.ShopType;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

public abstract class AbstractShop extends DungeonObject {
    // Fields
    private final ShopType shopType;

    // Constructors
    public AbstractShop(final ShopType newShopType) {
	super(false, false);
	this.shopType = newShopType;
	this.type.set(DungeonObjectTypes.TYPE_SHOP);
    }

    @Override
    public int getCustomProperty(final int propID) {
	return DungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    public void interactAction() {
	final var shop = DungeonDiver7.getStuffBag().getShopByType(this.shopType);
	if (shop != null) {
	    shop.showShop();
	}
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(Sounds.STEP_PARTY);
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
