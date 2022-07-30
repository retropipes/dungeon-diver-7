/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractAllButtonDoor;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;

public class PlasticAllButtonDoor extends AbstractAllButtonDoor {
    // Constructors
    public PlasticAllButtonDoor() {
	super();
	this.setMaterial(MaterialConstants.MATERIAL_PLASTIC);
    }

    @Override
    public final int getStringBaseID() {
	return 99;
    }
}