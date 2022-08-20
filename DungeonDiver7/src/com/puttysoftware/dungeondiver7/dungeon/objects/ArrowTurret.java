/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.GameActions;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class ArrowTurret extends AbstractMovableObject {
    // Fields
    private boolean autoMove;
    private boolean canShoot;

    // Constructors
    public ArrowTurret() {
	super(true);
	this.setDirection(Directions.NORTH);
	this.setFrameNumber(1);
	this.activateTimer(1);
	this.canShoot = true;
	this.autoMove = false;
	this.type.set(DungeonObjectTypes.TYPE_ANTI);
    }

    public void kill(final int locX, final int locY) {
	if (this.canShoot) {
	    DungeonDiver7.getStuffBag().getGameLogic().setLaserType(ShotTypes.RED);
	    DungeonDiver7.getStuffBag().getGameLogic().fireLaser(locX, locY, this);
	    this.canShoot = false;
	}
    }

    @Override
    public boolean canShoot() {
	return true;
    }

    @Override
    public void laserDoneAction() {
	this.canShoot = true;
    }

    @Override
    public boolean acceptTick(final int actionType) {
	return actionType == GameActions.MOVE;
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Directions baseDir = this.getDirection();
	if (laserType == ShotTypes.MISSILE || laserType == ShotTypes.POWER) {
	    final DeadArrowTurret dat = new DeadArrowTurret();
	    dat.setSavedObject(this.getSavedObject());
	    dat.setDirection(baseDir);
	    GameLogic.morph(dat, locX, locY, locZ, this.getLayer());
	    SoundLoader.playSound(SoundConstants.ANTI_DIE);
	    return Directions.NONE;
	} else if (laserType == ShotTypes.STUNNER) {
	    final StunnedArrowTurret sat = new StunnedArrowTurret();
	    sat.setSavedObject(this.getSavedObject());
	    sat.setDirection(baseDir);
	    GameLogic.morph(sat, locX, locY, locZ, this.getLayer());
	    SoundLoader.playSound(SoundConstants.STUN);
	    return Directions.NONE;
	} else {
	    final Directions sourceDir = DirectionResolver.resolveInvert(dirX, dirY);
	    if (sourceDir == baseDir) {
		final DeadArrowTurret dat = new DeadArrowTurret();
		dat.setSavedObject(this.getSavedObject());
		dat.setDirection(baseDir);
		GameLogic.morph(dat, locX, locY, locZ, this.getLayer());
		SoundLoader.playSound(SoundConstants.ANTI_DIE);
		return Directions.NONE;
	    } else {
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	if (this.getSavedObject().isOfType(DungeonObjectTypes.TYPE_ANTI_MOVER)) {
	    final Directions moveDir = this.getSavedObject().getDirection();
	    final int[] unres = DirectionResolver.unresolve(moveDir);
	    if (GameLogic.canObjectMove(locX, locY, unres[0], unres[1])) {
		if (this.autoMove) {
		    this.autoMove = false;
		    DungeonDiver7.getStuffBag().getGameLogic().updatePushedPosition(locX, locY, locX + unres[0],
			    locY + unres[1], this);
		}
	    } else {
		this.autoMove = true;
	    }
	}
	this.activateTimer(1);
    }

    @Override
    public final int getBaseID() {
	return 0;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.PUSH_ANTI_TANK);
    }
}
