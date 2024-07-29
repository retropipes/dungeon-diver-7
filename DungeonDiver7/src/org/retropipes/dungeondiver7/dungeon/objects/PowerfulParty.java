/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.GameActions;

public class PowerfulParty extends AbstractCharacter {
    public PowerfulParty(final Direction dir, final int number) {
	super(number);
	this.setDirection(dir);
	this.activateTimer(50);
    }

    // Constructors
    public PowerfulParty(final int number) {
	super(number);
	this.activateTimer(50);
    }

    @Override
    public boolean acceptTick(final int actionType) {
	return actionType == GameActions.MOVE;
    }

    @Override
    public final int getBaseID() {
	return 138;
    }

    @Override
    public void timerExpiredAction(final int x, final int y) {
	SoundLoader.playSound(Sounds.DISRUPT_END);
	DungeonDiver7.getStuffBag().getGameLogic().setNormalPlayer();
    }
}