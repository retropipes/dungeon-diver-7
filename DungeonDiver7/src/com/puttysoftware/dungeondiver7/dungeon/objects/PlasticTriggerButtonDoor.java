/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTriggerButtonDoor;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;

public class PlasticTriggerButtonDoor extends AbstractTriggerButtonDoor {
    // Constructors
    public PlasticTriggerButtonDoor() {
	super();
	this.setMaterial(MaterialConstants.MATERIAL_PLASTIC);
    }

    @Override
    public final int getStringBaseID() {
	return 103;
    }
}