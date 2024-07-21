/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.Materials;

public class MetallicRotaryMirror extends AbstractReactionWall {
	// Constructors
	public MetallicRotaryMirror() {
		this.setDirection(Direction.NORTH_EAST);
		this.setDiagonalOnly(true);
		this.setMaterial(Materials.METALLIC);
	}

	@Override
	public boolean doLasersPassThrough() {
		return true;
	}

	@Override
	public final int getBaseID() {
		return 66;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final int laserType, final int forceUnits) {
		final var dir = DirectionResolver.resolveInvert(dirX, dirY);
		if (AbstractDungeonObject.hitReflectiveSide(dir)) {
			// Reflect laser
			return this.getDirection();
		}
		// Rotate mirror
		this.toggleDirection();
		SoundLoader.playSound(Sounds.ROTATE);
		return Direction.NONE;
	}

	@Override
	public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType) {
		// Finish reflecting laser
		SoundLoader.playSound(Sounds.REFLECT);
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
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int rangeType, final int forceUnits) {
		// Rotate mirror
		this.toggleDirection();
		SoundLoader.playSound(Sounds.ROTATE);
		DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().markAsDirty(locX + dirX, locY + dirY, locZ);
		return true;
	}
}
