/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMover;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

public class PartyMover extends AbstractMover {
    // Constructors
    public PartyMover() {
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
	this.type.set(DungeonObjectTypes.TYPE_MOVER);
    }

    @Override
    public final int getIdValue() {
	return 37;
    }
}