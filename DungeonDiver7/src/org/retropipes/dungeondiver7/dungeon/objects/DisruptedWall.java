/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class DisruptedWall extends AbstractDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedWall() {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.disruptionLeft = DisruptedWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Materials.METALLIC);
    }

    DisruptedWall(final int disruption) {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.disruptionLeft = disruption;
	this.activateTimer(1);
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> {
	    final var diw = new DisruptedIcyWall(this.disruptionLeft);
	    diw.setPreviousState(this);
	    yield diw;
	}
	case Materials.FIRE -> new DisruptedHotWall(this.disruptionLeft);
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 52;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Heat up wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedHotWall(this.disruptionLeft), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	if (laserType == ShotTypes.STUNNER) {
	    // Freeze wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedIcyWall(this.disruptionLeft), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	// Stop laser
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Wall(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}