/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Tunnel;
import com.puttysoftware.dungeondiver7.utility.ColorConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractTunnel extends AbstractDungeonObject {
    // Fields
    private final static boolean[] tunnelsFull = new boolean[ColorConstants.COLOR_COUNT];
    private final static int SCAN_RADIUS = 24;

    // Constructors
    protected AbstractTunnel() {
	super(false, false, true);
	this.type.set(TypeConstants.TYPE_TUNNEL);
    }

    // Static methods
    public static void checkTunnels() {
	for (int x = 0; x < ColorConstants.COLOR_COUNT; x++) {
	    AbstractTunnel.checkTunnelsOfColor(x);
	}
    }

    public static boolean tunnelsFull(final int color) {
	return AbstractTunnel.tunnelsFull[color];
    }

    private static void checkTunnelsOfColor(final int color) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final int tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final int ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final int[] pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(0, 0, 0,
		AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(color), false);
	if (pgrmdest != null) {
	    AbstractTunnel.tunnelsFull[color] = false;
	} else {
	    AbstractTunnel.tunnelsFull[color] = true;
	}
    }

    // Scriptability
    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final int tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final int ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final int[] pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(dirX, dirY, dirZ,
		AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(this.getColor()), true);
	if (pgrmdest != null) {
	    app.getGameLogic().updatePositionAbsoluteNoEvents(pgrmdest[0], pgrmdest[1], pgrmdest[2]);
	}
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final int tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final int ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final int color = this.getColor();
	final int[] pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(x, y, z,
		AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(this.getColor()), false);
	if (pgrmdest != null) {
	    AbstractTunnel.tunnelsFull[color] = false;
	    app.getGameLogic().updatePushedIntoPositionAbsolute(pgrmdest[0], pgrmdest[1], pgrmdest[2], x, y, z,
		    pushed, this);
	} else {
	    AbstractTunnel.tunnelsFull[color] = true;
	    pushed.setWaitingOnTunnel(true);
	}
	return false;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    private static AbstractTunnel getTunnelOfColor(final int color) {
	return new Tunnel(color);
    }
}
