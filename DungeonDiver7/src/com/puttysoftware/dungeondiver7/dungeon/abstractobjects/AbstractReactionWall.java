/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.utilities.Direction;

public abstract class AbstractReactionWall extends AbstractWall {
    // Constructors
    protected AbstractReactionWall() {
	super();
    }

    @Override
    public final Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    return this.laserEnteredActionHook(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	} else {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public final boolean rangeAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    return this.rangeActionHook(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
	} else {
	    return super.rangeAction(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
	}
    }

    public abstract Direction laserEnteredActionHook(int locX, int locY, int locZ, int dirX, int dirY, int laserType,
	    int forceUnits);

    public abstract boolean rangeActionHook(int locX, int locY, int locZ, int dirX, int dirY, int laserType,
	    int forceUnits);
}