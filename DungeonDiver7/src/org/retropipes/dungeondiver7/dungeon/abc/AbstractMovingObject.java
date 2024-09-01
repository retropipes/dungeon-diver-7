/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractMovingObject extends DungeonObject {
    // Constructors
    public AbstractMovingObject(final boolean solid) {
	super(solid, false);
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
    public boolean isMoving() {
	return true;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	this.postMoveActionHook();
    }

    public void postMoveActionHook() {
	// Do nothing
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
