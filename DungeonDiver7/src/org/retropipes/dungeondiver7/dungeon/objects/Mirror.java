/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class Mirror extends AbstractMovableObject {
    // Constructors
    public Mirror() {
	super(true);
	this.setDirection(Direction.NORTH_EAST);
	this.setDiagonalOnly(true);
	this.type.set(DungeonObjectTypes.TYPE_MOVABLE_MIRROR);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getBaseID() {
	return 25;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy mirror
	    SoundLoader.playSound(Sounds.KABOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	final var dir = DirectionResolver.resolveInvert(dirX, dirY);
	if (AbstractDungeonObject.hitReflectiveSide(dir)) {
	    // Reflect laser
	    return this.getDirection();
	}
	// Move mirror
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	// Finish reflecting laser
	final var oldlaser = DirectionResolver.resolveInvert(locX, locY);
	final var currdir = this.getDirection();
	if (oldlaser == Direction.NORTH) {
	    if (currdir == Direction.NORTH_WEST) {
		return Direction.WEST;
	    }
	    if (currdir == Direction.NORTH_EAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.SOUTH) {
	    if (currdir == Direction.SOUTH_WEST) {
		return Direction.WEST;
	    }
	    if (currdir == Direction.SOUTH_EAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.WEST) {
	    if (currdir == Direction.SOUTH_WEST) {
		return Direction.SOUTH;
	    }
	    if (currdir == Direction.NORTH_WEST) {
		return Direction.NORTH;
	    }
	} else if (oldlaser == Direction.EAST) {
	    if (currdir == Direction.SOUTH_EAST) {
		return Direction.SOUTH;
	    }
	    if (currdir == Direction.NORTH_EAST) {
		return Direction.NORTH;
	    }
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}
