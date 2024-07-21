/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractField;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class FrostField extends AbstractField {
	// Constructors
	public FrostField() {
	}

	@Override
	public final int getBaseID() {
		return 14;
	}

	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		SoundLoader.playSound(Sounds.FROZEN);
		DungeonDiver7.getStuffBag().getGameLogic().updatePositionRelativeFrozen();
	}
}