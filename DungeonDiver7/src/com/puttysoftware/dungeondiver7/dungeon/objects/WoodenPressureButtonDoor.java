/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractPressureButtonDoor;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;

public class WoodenPressureButtonDoor extends AbstractPressureButtonDoor {
    // Constructors
    public WoodenPressureButtonDoor() {
	super();
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
    }

    @Override
    public final int getBaseID() {
	return 119;
    }
}