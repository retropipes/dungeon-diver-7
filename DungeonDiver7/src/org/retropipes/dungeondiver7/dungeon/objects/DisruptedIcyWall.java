/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class DisruptedIcyWall extends AbstractDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedIcyWall() {
	this.disruptionLeft = DisruptedIcyWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Materials.ICE);
    }

    DisruptedIcyWall(final int disruption) {
	this.disruptionLeft = disruption;
	this.activateTimer(1);
	this.setMaterial(Materials.ICE);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.FIRE -> new DisruptedWall(this.disruptionLeft);
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 59;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType != ShotTypes.MISSILE) {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Defrost icy wall
	final var dw = new DisruptedWall();
	if (this.hasPreviousState()) {
	    dw.setPreviousStateObject(this.getPreviousStateObject());
	}
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(dw, locX, locY, locZ, this.getLayer());
	return Direction.NONE;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    final var iw = new IcyWall();
	    if (this.hasPreviousState()) {
		iw.setPreviousStateObject(this.getPreviousStateObject());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(iw, locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}