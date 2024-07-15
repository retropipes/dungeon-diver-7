/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class RollingBarrelHorizontal extends AbstractMovableObject {
	// Constructors
	public RollingBarrelHorizontal() {
		super(true);
		this.type.set(DungeonObjectTypes.TYPE_BARREL);
		this.type.set(DungeonObjectTypes.TYPE_ICY);
	}

	@Override
	public final int getBaseID() {
		return 140;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		final var dir = DirectionResolver.resolve(dirX, dirY);
		if (dir == Direction.EAST || dir == Direction.WEST) {
			// Roll
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		// Break up
		final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
		// Boom!
		SoundLoader.playSound(Sounds.BARREL);
		DungeonDiver7.getStuffBag().getGameLogic();
		// Destroy barrel
		GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
		// Check for tank in range of explosion
		final var target = a.circularScanPlayer(locX, locY, locZ, 1);
		if (target) {
			// Kill tank
			DungeonDiver7.getStuffBag().getGameLogic().gameOver();
		}
		if (laserType == ShotTypes.POWER) {
			// Laser keeps going
			return DirectionResolver.resolve(dirX, dirY);
		}
		// Laser stops
		return Direction.NONE;
	}

	@Override
	public void playSoundHook() {
		SoundLoader.playSound(Sounds.BALL_ROLL);
	}

	@Override
	public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		// Break up
		final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
		// Boom!
		SoundLoader.playSound(Sounds.BARREL);
		DungeonDiver7.getStuffBag().getGameLogic();
		// Destroy barrel
		GameLogic.morph(new Empty(), x, y, z, this.getLayer());
		// Check for tank in range of explosion
		final var target = a.circularScanPlayer(x, y, z, 1);
		if (target) {
			// Kill tank
			DungeonDiver7.getStuffBag().getGameLogic().gameOver();
		}
	}
}