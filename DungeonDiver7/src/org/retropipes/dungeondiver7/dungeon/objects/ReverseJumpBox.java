/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import java.awt.Color;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractJumpObject;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;

public class ReverseJumpBox extends AbstractJumpObject {
	// Constructors
	public ReverseJumpBox() {
		this.type.set(DungeonObjectTypes.TYPE_BOX);
		this.setMaterial(Materials.STONE);
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		return switch (materialID) {
		case Materials.ICE -> {
			final var ib = new IcyBox();
			ib.setPreviousState(this);
			yield ib;
		}
		case Materials.FIRE -> new HotBox();
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
	public final int getBaseID() {
		return 124;
	}

	@Override
	public final Color getCustomTextColor() {
		return Color.black;
	}
}