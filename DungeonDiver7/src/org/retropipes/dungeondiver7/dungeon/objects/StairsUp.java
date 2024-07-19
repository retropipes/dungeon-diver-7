/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractTeleport;
import org.retropipes.dungeondiver7.loader.SoundLoader;
import org.retropipes.dungeondiver7.loader.Sounds;

public class StairsUp extends AbstractTeleport {
	// Constructors
	public StairsUp() {
	}

	@Override
	public final int getBaseID() {
		return 33;
	}

	@Override
	public int getDestinationFloor() {
		final var app = DungeonDiver7.getStuffBag();
		return app.getGameLogic().getPlayerManager().getPlayerLocationZ() + 1;
	}

	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		final var app = DungeonDiver7.getStuffBag();
		app.getGameLogic().updatePositionAbsoluteNoEvents(this.getDestinationFloor());
		SoundLoader.playSound(Sounds.UP);
	}
}
