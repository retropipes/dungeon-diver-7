/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractWall;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class WoodenWall extends AbstractWall {
    // Constructors
    public WoodenWall() {
	super();
	this.type.set(TypeConstants.TYPE_PLAIN_WALL);
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ArrowTypeConstants.LASER_TYPE_DISRUPTOR) {
	    // Disrupt wooden wall
	    SoundLoader.playSound(SoundConstants.SOUND_DISRUPTED);
	    DungeonDiver7.getApplication().getGameManager().morph(new DisruptedWoodenWall(), locX, locY, locZ,
		    this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Destroy wooden wall
	    SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_STUNNER) {
	    // Freeze wooden wall
	    SoundLoader.playSound(SoundConstants.SOUND_FROZEN);
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
    public final int getStringBaseID() {
	return 56;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_FIRE:
	    return new Ground();
	case MaterialConstants.MATERIAL_ICE:
	    final IcyWall iw = new IcyWall();
	    iw.setPreviousState(this);
	    return iw;
	default:
	    return this;
	}
    }
}