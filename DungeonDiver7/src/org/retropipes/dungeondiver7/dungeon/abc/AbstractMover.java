/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractMover extends AbstractGround {
	// Constructors
	protected AbstractMover() {
	}

	protected AbstractMover(final boolean hasFriction) {
		super(hasFriction);
	}

	@Override
	public int getLayer() {
		return DungeonConstants.LAYER_UPPER_GROUND;
	}
}
