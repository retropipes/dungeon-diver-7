/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTransientObject;

public class Stunner extends AbstractTransientObject {
    // Constructors
    public Stunner() {
    }

    @Override
    public final int getIdValue() {
	return 35;
    }

    @Override
    public int getForceUnitsImbued() {
	return 0;
    }
}
