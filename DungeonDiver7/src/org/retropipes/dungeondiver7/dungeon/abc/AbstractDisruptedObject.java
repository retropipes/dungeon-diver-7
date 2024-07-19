/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.GameActions;

public abstract class AbstractDisruptedObject extends AbstractPassThroughObject {
	// Constructors
	protected AbstractDisruptedObject() {
	}

	@Override
	public boolean acceptTick(final int actionType) {
		return actionType == GameActions.MOVE;
	}
}