/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.gameobject.Material;

public class Ground extends AbstractGround {
    // Constructors
    public Ground() {
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var i = new Ice();
	    i.setPreviousStateObject(this);
	    yield i;
	}
	case Material.FIRE -> new Lava();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 19;
    }
}