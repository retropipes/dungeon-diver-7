/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
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
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt icy wall
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    final var diw = new DisruptedIcyWall();
	    if (this.hasPreviousState()) {
		diw.setPreviousState(this.getPreviousState());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(diw, locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	}
	if (laserType != ShotTypes.MISSILE) {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Defrost icy wall
	SoundLoader.playSound(SoundConstants.DEFROST);
	AbstractDungeonObject ao;
	if (this.hasPreviousState()) {
	    ao = this.getPreviousState();
	} else {
	    ao = new Wall();
	}
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(ao, locX, locY, locZ, this.getLayer());
	return Directions.NONE;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    } else {
		return new Wall();
	    }
	default:
	    return this;
	}
    }

    @Override
    public final int getBaseID() {
	return 58;
    }
}