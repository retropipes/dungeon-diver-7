/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractTransientObject extends AbstractDungeonObject {
    // Constructors
    protected AbstractTransientObject() {
	super(true);
    }

    // Methods
    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_VIRTUAL;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    public abstract int getForceUnitsImbued();

    @Override
    public int getBlockHeight() {
	return 0;
    }
}
