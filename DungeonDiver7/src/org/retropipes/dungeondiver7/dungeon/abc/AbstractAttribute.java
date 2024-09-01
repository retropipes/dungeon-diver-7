/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractAttribute extends AbstractPassThroughObject {
    // Constructors
    protected AbstractAttribute() {
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_UPPER_OBJECTS;
    }
}