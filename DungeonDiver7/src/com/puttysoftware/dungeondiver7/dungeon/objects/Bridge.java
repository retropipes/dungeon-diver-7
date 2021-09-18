/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractGround;

public class Bridge extends AbstractGround {
	// Constructors
	public Bridge() {
		super();
	}

	@Override
	public final int getStringBaseID() {
		return 9;
	}
}