/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class Ground extends AbstractGround {
    // Constructors
    public Ground() {
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public final int getBaseID() {
	return 19;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.ICE:
	    final var i = new Ice();
	    i.setPreviousState(this);
	    return i;
	case Materials.FIRE:
	    return new Lava();
	default:
	    return this;
	}
    }
}