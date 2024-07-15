/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public abstract class AbstractWall extends AbstractDungeonObject {
	// Constructors
	protected AbstractWall() {
		super(true);
		this.type.set(DungeonObjectTypes.TYPE_WALL);
		this.setMaterial(Materials.STONE);
	}

	@Override
	public boolean doLasersPassThrough() {
		return false;
	}

	@Override
	public int getCustomProperty(final int propID) {
		return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
	}

	@Override
	public int getLayer() {
		return DungeonConstants.LAYER_LOWER_OBJECTS;
	}

	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		// Do nothing
	}

	@Override
	public void setCustomProperty(final int propID, final int value) {
		// Do nothing
	}
}