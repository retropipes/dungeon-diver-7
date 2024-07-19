/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractAttribute;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.SoundLoader;
import org.retropipes.dungeondiver7.loader.Sounds;

public class Cracked extends AbstractAttribute {
	// Constructors
	public Cracked() {
	}

	@Override
	public final int getBaseID() {
		return 131;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		final var app = DungeonDiver7.getStuffBag();
		app.getGameLogic();
		GameLogic.morph(new Damaged(), locX, locY, locZ, this.getLayer());
		SoundLoader.playSound(Sounds.CRACK);
		return Direction.NONE;
	}

	@Override
	public void moveFailedAction(final int locX, final int locY, final int locZ) {
		final var app = DungeonDiver7.getStuffBag();
		app.getGameLogic();
		GameLogic.morph(new Damaged(), locX, locY, locZ, this.getLayer());
		SoundLoader.playSound(Sounds.CRACK);
	}
}