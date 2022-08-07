/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractGround extends AbstractDungeonObject {
    // Constructors
    protected AbstractGround() {
	super(false, true, false);
    }

    protected AbstractGround(final boolean hasFriction) {
	super(false, hasFriction, false);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_GROUND;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_GROUND);
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	// Do nothing
    }
}
