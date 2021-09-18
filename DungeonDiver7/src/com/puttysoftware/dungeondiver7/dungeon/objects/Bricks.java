/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;

public class Bricks extends AbstractReactionWall {
	// Constructors
	public Bricks() {
		super();
		this.setMaterial(MaterialConstants.MATERIAL_PLASTIC);
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final int laserType, final int forceUnits) {
		SoundLoader.playSound(SoundConstants.SOUND_BREAK_BRICKS);
		DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
		if (laserType == ArrowTypeConstants.LASER_TYPE_POWER) {
			// Laser keeps going
			return DirectionResolver.resolveRelativeDirection(dirX, dirY);
		} else {
			// Laser stops
			return Direction.NONE;
		}
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int rangeType, final int forceUnits) {
		SoundLoader.playSound(SoundConstants.SOUND_BREAK_BRICKS);
		DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
		return true;
	}

	@Override
	public final int getStringBaseID() {
		return 8;
	}
}