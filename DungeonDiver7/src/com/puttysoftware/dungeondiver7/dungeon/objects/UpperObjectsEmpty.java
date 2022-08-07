/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class UpperObjectsEmpty extends AbstractPassThroughObject {
    // Constructors
    public UpperObjectsEmpty() {
	super();
	this.type.set(TypeConstants.TYPE_EMPTY_SPACE);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_UPPER_OBJECTS;
    }

    @Override
    public final int getBaseID() {
	return 130;
    }
}