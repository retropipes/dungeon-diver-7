/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDoor;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.PartyInventory;

public class RedDoor extends AbstractDoor {
	// Constructors
	public RedDoor() {
		super(new RedKey());
	}

	// Scriptability
	@Override
	public boolean isConditionallySolid() {
		return PartyInventory.getRedKeysLeft() < 1;
	}

	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		SoundLoader.playSound(SoundConstants.SOUND_UNLOCK);
		PartyInventory.useRedKey();
		DungeonDiver7.getApplication().getGameManager().morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
	}

	@Override
	public final int getStringBaseID() {
		return 28;
	}
}