/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.integration1.dungeon.DungeonConstants;

public abstract class AbstractMPModifier extends AbstractGameObject {
    // Constructors
    protected AbstractMPModifier() {
	super(false, false);
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_MP_MODIFIER);
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
