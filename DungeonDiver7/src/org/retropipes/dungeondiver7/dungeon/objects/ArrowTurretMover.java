/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMover;

public class ArrowTurretMover extends AbstractMover {
    // Constructors
    public ArrowTurretMover() {
	super(true);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
    }

    @Override
    public final int getIdValue() {
	return 1;
    }
}