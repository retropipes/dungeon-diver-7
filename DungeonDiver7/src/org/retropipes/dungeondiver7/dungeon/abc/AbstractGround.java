/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractGround extends GameObject {
    // Constructors
    protected AbstractGround() {
	super(false, false, true);
    }

    protected AbstractGround(final boolean hasFriction) {
	super(false, false, hasFriction);
    }

    @Override
    public int getHeight() {
	return 0;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return GameObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_GROUND;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
