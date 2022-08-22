/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class RotaryMirror extends AbstractReactionWall {
    // Constructors
    public RotaryMirror() {
	this.setDirection(Directions.NORTHEAST);
	this.setDiagonalOnly(true);
    }

    @Override
    public Directions laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy mirror
	    SoundLoader.playSound(SoundConstants.BOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	}
	final var dir = DirectionResolver.resolveInvert(dirX, dirY);
	if (AbstractDungeonObject.hitReflectiveSide(dir)) {
	    // Reflect laser
	    return this.getDirection();
	}
	// Rotate mirror
	this.toggleDirection();
	SoundLoader.playSound(SoundConstants.ROTATE);
	return Directions.NONE;
    }

    @Override
    public Directions laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	// Finish reflecting laser
	SoundLoader.playSound(SoundConstants.REFLECT);
	final var oldlaser = DirectionResolver.resolveInvert(locX, locY);
	final var currdir = this.getDirection();
	if (oldlaser == Directions.NORTH) {
	    if (currdir == Directions.NORTHWEST) {
		return Directions.WEST;
	    }
	    if (currdir == Directions.NORTHEAST) {
		return Directions.EAST;
	    }
	} else if (oldlaser == Directions.SOUTH) {
	    if (currdir == Directions.SOUTHWEST) {
		return Directions.WEST;
	    }
	    if (currdir == Directions.SOUTHEAST) {
		return Directions.EAST;
	    }
	} else if (oldlaser == Directions.WEST) {
	    if (currdir == Directions.SOUTHWEST) {
		return Directions.SOUTH;
	    }
	    if (currdir == Directions.NORTHWEST) {
		return Directions.NORTH;
	    }
	} else if (oldlaser == Directions.EAST) {
	    if (currdir == Directions.SOUTHEAST) {
		return Directions.SOUTH;
	    }
	    if (currdir == Directions.NORTHEAST) {
		return Directions.NORTH;
	    }
	}
	return Directions.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	// Rotate mirror
	this.toggleDirection();
	SoundLoader.playSound(SoundConstants.ROTATE);
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().markAsDirty(locX + dirX, locY + dirY, locZ);
	return true;
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getBaseID() {
	return 31;
    }
}
