/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.objects.Tunnel;
import org.retropipes.dungeondiver7.locale.Colors;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

public abstract class AbstractTunnel extends AbstractDungeonObject {
    // Fields
    private final static boolean[] tunnelsFull = new boolean[Strings.COLOR_COUNT];
    private final static int SCAN_RADIUS = 24;

    // Static methods
    public static void checkTunnels() {
	for (var x = 0; x < Strings.COLOR_COUNT; x++) {
	    AbstractTunnel.checkTunnelsOfColor(Colors.values()[x]);
	}
    }

    private static void checkTunnelsOfColor(final Colors color) {
	final var app = DungeonDiver7.getStuffBag();
	final var tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final var ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final var pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(0, 0, 0,
		AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(color), false);
	if (pgrmdest != null) {
	    AbstractTunnel.tunnelsFull[color.ordinal()] = false;
	} else {
	    AbstractTunnel.tunnelsFull[color.ordinal()] = true;
	}
    }

    private static AbstractTunnel getTunnelOfColor(final Colors color) {
	return new Tunnel(color);
    }

    public static boolean tunnelsFull(final Colors color) {
	return AbstractTunnel.tunnelsFull[color.ordinal()];
    }

    // Constructors
    protected AbstractTunnel() {
	super(false, false, true);
	this.type.set(DungeonObjectTypes.TYPE_TUNNEL);
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
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
		AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(this.getColor()), true);
	if (pgrmdest != null) {
	    app.getGameLogic().updatePositionAbsoluteNoEvents(pgrmdest[0], pgrmdest[1], pgrmdest[2]);
	}
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	final var tx = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final var ty = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	final var color = this.getColor();
	final var pgrmdest = app.getDungeonManager().getDungeon().circularScanTunnel(x, y, z,
		AbstractTunnel.SCAN_RADIUS, tx, ty, AbstractTunnel.getTunnelOfColor(this.getColor()), false);
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
