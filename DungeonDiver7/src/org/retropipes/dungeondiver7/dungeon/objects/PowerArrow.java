/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTransientObject;

public class PowerArrow extends AbstractTransientObject {
	// Constructors
	public PowerArrow() {
	}

	@Override
	public final int getBaseID() {
		return 137;
	}

	@Override
	public int getForceUnitsImbued() {
		return 5;
	}
}
