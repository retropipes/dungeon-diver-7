/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ActionConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;

public class ArrowTurretDisguise extends AbstractCharacter {
    // Fields
    private int disguiseLeft;
    private static final int DISGUISE_LENGTH = 30;

    // Constructors
    public ArrowTurretDisguise(final int number) {
	super(number);
	this.disguiseLeft = ArrowTurretDisguise.DISGUISE_LENGTH;
	this.activateTimer(1);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
    }

    public ArrowTurretDisguise(final Direction dir, final int number) {
	super(number);
	this.disguiseLeft = ArrowTurretDisguise.DISGUISE_LENGTH;
	this.activateTimer(1);
	this.setDirection(dir);
	this.setFrameNumber(1);
    }

    @Override
    public final int getBaseID() {
	return 0;
    }

    @Override
    public boolean acceptTick(final int actionType) {
	return actionType == ActionConstants.ACTION_MOVE;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disguiseLeft--;
	if (this.disguiseLeft == 0) {
	    SoundLoader.playSound(SoundConstants.DISRUPT_END);
	    DungeonDiver7.getApplication().getGameManager().setNormalPlayer();
	} else {
	    this.activateTimer(1);
	}
    }
}