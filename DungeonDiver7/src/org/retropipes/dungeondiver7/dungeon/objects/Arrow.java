/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTransientObject;

public class Arrow extends AbstractTransientObject {
    // Constructors
    public Arrow() {
    }

    @Override
    public final int getBaseID() {
	return 18;
    }

    @Override
    public int getForceUnitsImbued() {
	return 1;
    }
}
