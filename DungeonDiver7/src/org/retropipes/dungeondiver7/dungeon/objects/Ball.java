/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;

public class Ball extends AbstractMovableObject {
    // Constructors
    public Ball() {
	super(true);
    }

    @Override
    public final int getIdValue() {
	return 2;
    }

    @Override
    public void playSoundHook() {
    }
}