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

public class Wall extends AbstractWall {
    // Constructors
    public Wall() {
	super();
	this.type.set(TypeConstants.TYPE_PLAIN_WALL);
	this.setMaterial(MaterialConstants.MATERIAL_METALLIC);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ArrowTypeConstants.LASER_TYPE_DISRUPTOR) {
	    // Disrupt wall
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    DungeonDiver7.getApplication().getGameManager().morph(new DisruptedWall(), locX, locY, locZ,
		    this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Heat up wall
	    SoundLoader.playSound(SoundConstants.MELT);
	    DungeonDiver7.getApplication().getGameManager().morph(new HotWall(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_STUNNER) {
	    // Freeze wall
	    SoundLoader.playSound(SoundConstants.FROZEN);
	    final IcyWall iw = new IcyWall();
	    iw.setPreviousState(this);
	    DungeonDiver7.getApplication().getGameManager().morph(iw, locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public final int getBaseID() {
	return 45;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_ICE:
	    final IcyWall iw = new IcyWall();
	    iw.setPreviousState(this);
	    return iw;
	case MaterialConstants.MATERIAL_FIRE:
	    return new HotWall();
	default:
	    return this;
	}
    }
}