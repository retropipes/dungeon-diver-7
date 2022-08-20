/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.GameActions;

public class PowerfulParty extends AbstractCharacter {
    // Constructors
    public PowerfulParty(final int number) {
	super(number);
	this.activateTimer(50);
    }

    public PowerfulParty(final Directions dir, final int number) {
	super(number);
	this.setDirection(dir);
	this.activateTimer(50);
    }

    @Override
    public boolean acceptTick(final int actionType) {
	return actionType == GameActions.MOVE;
    }

    @Override
    public void timerExpiredAction(final int x, final int y) {
	SoundLoader.playSound(SoundConstants.DISRUPT_END);
	DungeonDiver7.getStuffBag().getGameLogic().setNormalPlayer();
    }

    @Override
    public final int getBaseID() {
	return 138;
    }
}