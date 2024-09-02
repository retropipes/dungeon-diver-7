/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractWall;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.gameobject.Material;

public class IcyWall extends AbstractWall {
    // Constructors
    public IcyWall() {
	this.setMaterial(Material.ICE);
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case Material.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousStateObject();
	    }
	    return new Wall();
	default:
	    return this;
	}
    }

    @Override
    public final int getIdValue() {
	return 58;
    }
}