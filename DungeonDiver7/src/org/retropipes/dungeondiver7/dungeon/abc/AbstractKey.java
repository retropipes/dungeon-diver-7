/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractKey extends DungeonObject {
    // Constructors
    protected AbstractKey() {
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
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
