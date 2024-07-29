/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractPressureButtonDoor;
import org.retropipes.dungeondiver7.utility.Materials;

public class MetallicPressureButtonDoor extends AbstractPressureButtonDoor {
    // Constructors
    public MetallicPressureButtonDoor() {
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public final int getBaseID() {
	return 95;
    }
}