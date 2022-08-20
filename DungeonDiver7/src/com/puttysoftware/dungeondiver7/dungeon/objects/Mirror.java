/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class Mirror extends AbstractMovableObject {
    // Constructors
    public Mirror() {
	super(true);
	this.setDirection(Directions.NORTHEAST);
	this.setDiagonalOnly(true);
	this.type.set(TypeConstants.TYPE_MOVABLE_MIRROR);
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Destroy mirror
	    SoundLoader.playSound(SoundConstants.BOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else {
	    final Directions dir = DirectionResolver.resolveInvert(dirX, dirY);
	    if (AbstractDungeonObject.hitReflectiveSide(dir)) {
		// Reflect laser
		return this.getDirection();
	    } else {
		// Move mirror
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	}
    }

    @Override
    public Directions laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	// Finish reflecting laser
	SoundLoader.playSound(SoundConstants.REFLECT);
	final Directions oldlaser = DirectionResolver.resolveInvert(locX, locY);
	final Directions currdir = this.getDirection();
	if (oldlaser == Directions.NORTH) {
	    if (currdir == Directions.NORTHWEST) {
		return Directions.WEST;
	    } else if (currdir == Directions.NORTHEAST) {
		return Directions.EAST;
	    }
	} else if (oldlaser == Directions.SOUTH) {
	    if (currdir == Directions.SOUTHWEST) {
		return Directions.WEST;
	    } else if (currdir == Directions.SOUTHEAST) {
		return Directions.EAST;
	    }
	} else if (oldlaser == Directions.WEST) {
	    if (currdir == Directions.SOUTHWEST) {
		return Directions.SOUTH;
	    } else if (currdir == Directions.NORTHWEST) {
		return Directions.NORTH;
	    }
	} else if (oldlaser == Directions.EAST) {
	    if (currdir == Directions.SOUTHEAST) {
		return Directions.SOUTH;
	    } else if (currdir == Directions.NORTHEAST) {
		return Directions.NORTH;
	    }
	}
	return Directions.NONE;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.PUSH_MIRROR);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getBaseID() {
	return 25;
    }
}
