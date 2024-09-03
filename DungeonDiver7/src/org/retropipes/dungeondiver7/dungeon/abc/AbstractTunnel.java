/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractTunnel extends GameObject {
    // Fields
    private final static boolean[] tunnelsFull = new boolean[Strings.COLOR_COUNT];
    private final static int SCAN_RADIUS = 24;

    // Constructors
    protected AbstractTunnel() {
	super(false, false, true);
    }

    @Override
    public int getCustomProperty(final int propID) {
	return GameObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    // Scriptability
    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final var app = DungeonDiver7.getStuffBag();
	final var tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final var ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final var pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(dirX, dirY, dirZ,
		AbstractTunnel.SCAN_RADIUS, tx, ty, GameObject.getTunnelOfColor(this.getColor()), true);
	if (pgrmdest != null) {
	    app.getGameLogic().updatePositionAbsoluteNoEvents(pgrmdest[0], pgrmdest[1], pgrmdest[2]);
	}
    }

    @Override
    public boolean pushIntoAction(final GameObject pushed, final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	final var tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final var ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final var color = this.getColor();
	final var pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(x, y, z,
		AbstractTunnel.SCAN_RADIUS, tx, ty, GameObject.getTunnelOfColor(this.getColor()), false);
	if (pgrmdest != null) {
	    AbstractTunnel.tunnelsFull[color.ordinal()] = false;
	    app.getGameLogic().updatePushedIntoPositionAbsolute(pgrmdest[0], pgrmdest[1], pgrmdest[2], x, y, z, pushed,
		    this);
	} else {
	    AbstractTunnel.tunnelsFull[color.ordinal()] = true;
	    pushed.setWaitingOnTunnel(true);
	}
	return false;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}
