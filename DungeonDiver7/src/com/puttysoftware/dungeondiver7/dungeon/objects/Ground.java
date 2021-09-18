/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractGround;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;

public class Ground extends AbstractGround {
	// Constructors
	public Ground() {
		super();
		this.setMaterial(MaterialConstants.MATERIAL_METALLIC);
	}

	@Override
	public final int getStringBaseID() {
		return 19;
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		switch (materialID) {
		case MaterialConstants.MATERIAL_ICE:
			final Ice i = new Ice();
			i.setPreviousState(this);
			return i;
		case MaterialConstants.MATERIAL_FIRE:
			return new Lava();
		default:
			return this;
		}
	}
}