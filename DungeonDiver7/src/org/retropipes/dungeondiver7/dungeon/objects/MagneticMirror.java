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
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class MagneticMirror extends AbstractMovableObject {
    // Constructors
    public MagneticMirror() {
	super(true);
	this.setDirection(Direction.NORTH_EAST);
	this.setDiagonalOnly(true);
	this.type.set(DungeonObjectTypes.TYPE_MOVABLE_MIRROR);
	this.setMaterial(Materials.MAGNETIC);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getBaseID() {
	return 23;
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
	final var app = DungeonDiver7.getStuffBag();
	final var mo = app.getDungeonManager().getDungeon().getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
	if (laserType == ShotTypes.BLUE && mo != null
		&& (mo.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mo.isSolid())) {
	    app.getGameLogic().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
	    this.playSoundHook();
	} else if (mo != null && (mo.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mo.isSolid())) {
	    app.getGameLogic().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
	    this.playSoundHook();
	} else if (laserType == ShotTypes.MISSILE) {
	    SoundLoader.playSound(Sounds.KABOOM);
	} else {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return Direction.NONE;
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
