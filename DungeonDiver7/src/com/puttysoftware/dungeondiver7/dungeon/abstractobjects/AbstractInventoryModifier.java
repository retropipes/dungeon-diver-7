/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;

public abstract class AbstractInventoryModifier extends AbstractGround {
    // Constructors
    protected AbstractInventoryModifier() {
	super();
	this.setMaterial(MaterialConstants.MATERIAL_NOT_APPLICABLE);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }
}
