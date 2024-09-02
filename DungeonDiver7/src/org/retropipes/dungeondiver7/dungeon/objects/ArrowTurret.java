/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.GameActions;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class ArrowTurret extends AbstractMovableObject {
    // Fields
    private boolean canShoot;

    // Constructors
    public ArrowTurret() {
	super(true);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
	this.activateTimer(1);
	this.canShoot = true;
    }

    @Override
    public boolean acceptsTick(final int actionType) {
	return actionType == GameActions.MOVE;
    }

    @Override
    public boolean canShoot() {
	return true;
    }

    @Override
    public final int getIdValue() {
	return 0;
    }

    public void kill(final int locX, final int locY) {
	if (this.canShoot) {
	    DungeonDiver7.getStuffBag().getGameLogic().setLaserType(ShotTypes.RED);
	    DungeonDiver7.getStuffBag().getGameLogic().fireLaser(locX, locY, this);
	    this.canShoot = false;
	}
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}
