/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

public abstract class AbstractTeleport extends DungeonObject {
    // Constructors
    protected AbstractTeleport() {
	super(false);
	this.type.set(DungeonObjectTypes.TYPE_TELEPORT);
    }

    @Override
    public int getCustomProperty(final int propID) {
	return DungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    public abstract int getDestinationFloor();

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
