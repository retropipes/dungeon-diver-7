/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import java.awt.Color;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractJumpObject;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.gameobject.Material;

public class ReverseJumpBox extends AbstractJumpObject {
    // Constructors
    public ReverseJumpBox() {
 	this.setMaterial(Material.STONE);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var ib = new IcyBox();
	    ib.setPreviousStateObject(this);
	    yield ib;
	}
	case Material.FIRE -> new HotBox();
	default -> this;
	};
    }

    @Override
    public int getActualJumpCols() {
	return -super.getActualJumpCols();
    }

    @Override
    public int getActualJumpRows() {
	return -super.getActualJumpRows();
    }

    @Override
    public final int getIdValue() {
	return 124;
    }

    @Override
    public final Color getCustomTextColor() {
	return Color.black;
    }
}