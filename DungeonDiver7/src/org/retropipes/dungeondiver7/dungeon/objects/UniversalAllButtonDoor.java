/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractAllButtonDoor;
import org.retropipes.dungeondiver7.utility.Materials;

public class UniversalAllButtonDoor extends AbstractAllButtonDoor {
    // Constructors
    public UniversalAllButtonDoor() {
	this.setMaterial(Materials.DEFAULT);
    }

    @Override
    public final int getId() {
	return 111;
    }
}