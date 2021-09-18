/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractPressureButtonDoor;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;

public class MagneticPressureButtonDoor extends AbstractPressureButtonDoor {
	// Constructors
	public MagneticPressureButtonDoor() {
		super();
		this.setMaterial(MaterialConstants.MATERIAL_MAGNETIC);
	}

	@Override
	public final int getStringBaseID() {
		return 89;
	}
}