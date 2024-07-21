/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class DeadArrowTurret extends AbstractMovableObject {
	// Constructors
	public DeadArrowTurret() {
		super(false);
		this.setDirection(Direction.NORTH);
	}

	@Override
	public final int getBaseID() {
		return 11;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		DungeonDiver7.getStuffBag().getGameLogic().haltMovingObjects();
		if (laserType != ShotTypes.MISSILE) {
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
		// Destroy
		SoundLoader.playSound(Sounds.BOOM);
		DungeonDiver7.getStuffBag().getGameLogic();
		GameLogic.morph(this.getSavedObject(), locX, locY, locZ, this.getLayer());
		return Direction.NONE;
	}

	@Override
	public void playSoundHook() {
		// Do nothing
	}
}
