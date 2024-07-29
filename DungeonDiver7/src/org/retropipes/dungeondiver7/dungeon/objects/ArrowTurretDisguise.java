/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;
import org.retropipes.dungeondiver7.utility.GameActions;

public class ArrowTurretDisguise extends AbstractCharacter {
    private static final int DISGUISE_LENGTH = 30;
    // Fields
    private int disguiseLeft;

    public ArrowTurretDisguise(final Direction dir, final int number) {
	super(number);
	this.disguiseLeft = ArrowTurretDisguise.DISGUISE_LENGTH;
	this.activateTimer(1);
	this.setDirection(dir);
	this.setFrameNumber(1);
    }

    // Constructors
    public ArrowTurretDisguise(final int number) {
	super(number);
	this.disguiseLeft = ArrowTurretDisguise.DISGUISE_LENGTH;
	this.activateTimer(1);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
    }

    @Override
    public boolean acceptTick(final int actionType) {
	return actionType == GameActions.MOVE;
    }

    @Override
    public final int getBaseID() {
	return 0;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disguiseLeft--;
	if (this.disguiseLeft == 0) {
	    DungeonDiver7.getStuffBag().getGameLogic().setNormalPlayer();
	} else {
	    this.activateTimer(1);
	}
    }
}