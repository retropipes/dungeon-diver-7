/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTransientObject;

public class Disruptor extends AbstractTransientObject {
    // Constructors
    public Disruptor() {
    }

    @Override
    public final int getBaseID() {
	return 47;
    }

    @Override
    public int getForceUnitsImbued() {
	return 0;
    }
}
