/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class MetallicMirror extends AbstractMovableObject {
    // Constructors
    public MetallicMirror() {
	super(true);
	this.setDirection(Direction.NORTHEAST);
	this.setDiagonalOnly(true);
	this.type.set(TypeConstants.TYPE_MOVABLE_MIRROR);
	this.setMaterial(MaterialConstants.MATERIAL_METALLIC);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Direction dir = DirectionResolver.resolveRelativeDirectionInvert(dirX, dirY);
	if (AbstractDungeonObject.hitReflectiveSide(dir)) {
	    // Reflect laser
	    return this.getDirection();
	} else {
	    // Move mirror
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	// Finish reflecting laser
	SoundLoader.playSound(SoundConstants.SOUND_REFLECT);
	final Direction oldlaser = DirectionResolver.resolveRelativeDirectionInvert(locX, locY);
	final Direction currdir = this.getDirection();
	if (oldlaser == Direction.NORTH) {
	    if (currdir == Direction.NORTHWEST) {
		return Direction.WEST;
	    } else if (currdir == Direction.NORTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.SOUTH) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.WEST;
	    } else if (currdir == Direction.SOUTHEAST) {
		return Direction.EAST;
	    }
	} else if (oldlaser == Direction.WEST) {
	    if (currdir == Direction.SOUTHWEST) {
		return Direction.SOUTH;
	    } else if (currdir == Direction.NORTHWEST) {
		return Direction.NORTH;
	    }
	} else if (oldlaser == Direction.EAST) {
	    if (currdir == Direction.SOUTHEAST) {
		return Direction.SOUTH;
	    } else if (currdir == Direction.NORTHEAST) {
		return Direction.NORTH;
	    }
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.SOUND_PUSH_MIRROR);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getStringBaseID() {
	return 65;
    }
}
