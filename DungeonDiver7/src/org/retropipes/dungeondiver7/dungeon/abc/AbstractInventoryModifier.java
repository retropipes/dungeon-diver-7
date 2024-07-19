/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.Materials;

public abstract class AbstractInventoryModifier extends AbstractGround {
	// Constructors
	protected AbstractInventoryModifier() {
		this.setMaterial(Materials.NOT_APPLICABLE);
	}

	@Override
	public int getLayer() {
		return DungeonConstants.LAYER_LOWER_OBJECTS;
	}
}
