/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractGround extends AbstractDungeonObject {
    // Constructors
    protected AbstractGround() {
	super(false, false, true);
	this.type.set(TypeConstants.TYPE_GROUND);
    }

    protected AbstractGround(final boolean hasFriction) {
	super(false, false, hasFriction);
	this.type.set(TypeConstants.TYPE_GROUND);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_GROUND;
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
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public int getBlockHeight() {
	return 0;
    }
}
