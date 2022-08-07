/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractAttribute extends AbstractPassThroughObject {
    // Constructors
    protected AbstractAttribute() {
	super();
	this.type.set(TypeConstants.TYPE_ATTRIBUTE);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_UPPER_OBJECTS;
    }
}