/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTransientObject;

public class DeathArrow extends AbstractTransientObject {
    // Constructors
    public DeathArrow() {
	super();
    }

    @Override
    public final int getStringBaseID() {
	return 30;
    }

    @Override
    public int getForceUnitsImbued() {
	return 1;
    }
}
