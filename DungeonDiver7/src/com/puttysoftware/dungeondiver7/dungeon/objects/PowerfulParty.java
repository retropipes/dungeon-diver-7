/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractCharacter;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.ActionConstants;
import com.puttysoftware.dungeondiver7.utilities.Direction;

public class PowerfulParty extends AbstractCharacter {
	// Constructors
	public PowerfulParty(final int number) {
		super(number);
		this.activateTimer(50);
	}

	public PowerfulParty(final Direction dir, final int number) {
		super(number);
		this.setDirection(dir);
		this.activateTimer(50);
	}

	@Override
	public boolean acceptTick(final int actionType) {
		return actionType == ActionConstants.ACTION_MOVE;
	}

	@Override
	public void timerExpiredAction(final int x, final int y) {
		SoundLoader.playSound(SoundConstants.SOUND_DISRUPT_END);
		DungeonDiver7.getApplication().getGameManager().setNormalTank();
	}

	@Override
	public final int getStringBaseID() {
		return 138;
	}
}