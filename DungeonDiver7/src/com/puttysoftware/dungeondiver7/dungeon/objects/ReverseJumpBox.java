/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import java.awt.Color;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractJumpObject;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class ReverseJumpBox extends AbstractJumpObject {
    // Constructors
    public ReverseJumpBox() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_BOX);
	this.setMaterial(Materials.STONE);
    }

    @Override
    public int getActualJumpRows() {
	return -super.getActualJumpRows();
    }

    @Override
    public int getActualJumpCols() {
	return -super.getActualJumpCols();
    }

    @Override
    public final int getBaseID() {
	return 124;
    }

    @Override
    public final Color getCustomTextColor() {
	return Color.black;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.ICE:
	    final IcyBox ib = new IcyBox();
	    ib.setPreviousState(this);
	    return ib;
	case Materials.FIRE:
	    return new HotBox();
	default:
	    return this;
	}
    }
}