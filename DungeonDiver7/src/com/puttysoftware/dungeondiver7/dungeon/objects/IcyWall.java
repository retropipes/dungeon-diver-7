/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class IcyWall extends AbstractWall {
    // Constructors
    public IcyWall() {
	super();
	this.type.set(TypeConstants.TYPE_PLAIN_WALL);
	this.setMaterial(MaterialConstants.MATERIAL_ICE);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ArrowTypeConstants.LASER_TYPE_DISRUPTOR) {
	    // Disrupt icy wall
	    SoundLoader.playSound(SoundConstants.SOUND_DISRUPTED);
	    final DisruptedIcyWall diw = new DisruptedIcyWall();
	    if (this.hasPreviousState()) {
		diw.setPreviousState(this.getPreviousState());
	    }
	    DungeonDiver7.getApplication().getGameManager().morph(diw, locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Defrost icy wall
	    SoundLoader.playSound(SoundConstants.SOUND_DEFROST);
	    AbstractDungeonObject ao;
	    if (this.hasPreviousState()) {
		ao = this.getPreviousState();
	    } else {
		ao = new Wall();
	    }
	    DungeonDiver7.getApplication().getGameManager().morph(ao, locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_FIRE:
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
    public final int getStringBaseID() {
	return 58;
    }
}