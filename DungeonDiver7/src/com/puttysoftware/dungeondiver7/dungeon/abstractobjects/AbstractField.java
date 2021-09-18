/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public abstract class AbstractField extends AbstractDungeonObject {
	// Constructors
	protected AbstractField() {
		super(false);
		this.type.set(TypeConstants.TYPE_FIELD);
	}

	@Override
	public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

	@Override
	public int getLayer() {
		return DungeonConstants.LAYER_LOWER_OBJECTS;
	}

	@Override
	public int getCustomProperty(final int propID) {
		return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
	}

	@Override
	public void setCustomProperty(final int propID, final int value) {
		// Do nothing
	}
}