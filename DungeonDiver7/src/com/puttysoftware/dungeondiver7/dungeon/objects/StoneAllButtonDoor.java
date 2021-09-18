/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractAllButtonDoor;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;

public class StoneAllButtonDoor extends AbstractAllButtonDoor {
	// Constructors
	public StoneAllButtonDoor() {
		super();
		this.setMaterial(MaterialConstants.MATERIAL_STONE);
	}

	@Override
	public final int getStringBaseID() {
		return 105;
	}
}