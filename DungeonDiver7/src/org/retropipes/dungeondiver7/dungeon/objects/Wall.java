/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractWall;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.gameobject.Material;

public class Wall extends AbstractWall {
    // Constructors
    public Wall() {
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var iw = new IcyWall();
	    iw.setPreviousStateObject(this);
	    yield iw;
	}
	case Material.FIRE -> new HotWall();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 45;
    }
}