/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;

public class DeadArrowTurret extends AbstractMovableObject {
    // Constructors
    public DeadArrowTurret() {
	super(false);
	this.setDirection(Direction.NORTH);
    }

    @Override
    public final int getIdValue() {
	return 11;
    }

    @Override
    public void playSoundHook() {
	// Do nothing
    }
}
