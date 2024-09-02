/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

public abstract class AbstractReactionWall extends AbstractWall {
    // Constructors
    protected AbstractReactionWall() {
    }

    @Override
    public final boolean rangeAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (forceUnits >= this.getMinimumReactionForce()) {
	    return this.rangeActionHook(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
	}
	return super.rangeAction(locX, locY, locZ, dirX, dirY, rangeType, forceUnits);
    }

    public boolean rangeActionHook(int locX, int locY, int locZ, int dirX, int dirY, int laserType,
	    int forceUnits) {
	// By default, do nothing
	return false;
    }
}