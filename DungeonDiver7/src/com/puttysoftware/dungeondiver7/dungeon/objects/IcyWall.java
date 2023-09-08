/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class IcyWall extends AbstractWall {
    // Constructors
    public IcyWall() {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.ICE);
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    }
	    return new Wall();
	default:
	    return this;
	}
    }

    @Override
    public final int getBaseID() {
	return 58;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt icy wall
	    SoundLoader.playSound(Sounds.DISRUPTED);
	    final var diw = new DisruptedIcyWall();
	    if (this.hasPreviousState()) {
		diw.setPreviousState(this.getPreviousState());
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
	SoundLoader.playSound(Sounds.DEFROST);
	AbstractDungeonObject ao;
	if (this.hasPreviousState()) {
	    ao = this.getPreviousState();
	} else {
	    ao = new Wall();
	}
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(ao, locX, locY, locZ, this.getLayer());
	return Direction.NONE;
    }
}