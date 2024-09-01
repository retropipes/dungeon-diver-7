/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractWall;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class IcyWall extends AbstractWall {
    // Constructors
    public IcyWall() {
	this.setMaterial(Materials.ICE);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousStateObject();
	    }
	    return new Wall();
	default:
	    return this;
	}
    }

    @Override
    public final int getIdValue() {
	return 58;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt icy wall
	    final var diw = new DisruptedIcyWall();
	    if (this.hasPreviousState()) {
		diw.setPreviousStateObject(this.getPreviousStateObject());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(diw, locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	if (laserType != ShotTypes.MISSILE) {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Defrost icy wall
	DungeonObject ao;
	if (this.hasPreviousState()) {
	    ao = this.getPreviousStateObject();
	} else {
	    ao = new Wall();
	}
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(ao, locX, locY, locZ, this.getLayer());
	return Direction.NONE;
    }
}