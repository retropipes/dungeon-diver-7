/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameManager;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ActionConstants;
import com.puttysoftware.dungeondiver7.utility.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class ArrowTurret extends AbstractMovableObject {
    // Fields
    private boolean autoMove;
    private boolean canShoot;

    // Constructors
    public ArrowTurret() {
	super(true);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
	this.activateTimer(1);
	this.canShoot = true;
	this.autoMove = false;
	this.type.set(TypeConstants.TYPE_ANTI);
    }

    public void kill(final int locX, final int locY) {
	if (this.canShoot) {
	    DungeonDiver7.getApplication().getGameManager().setLaserType(ArrowTypeConstants.LASER_TYPE_RED);
	    DungeonDiver7.getApplication().getGameManager().fireLaser(locX, locY, this);
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
	return actionType == ActionConstants.ACTION_MOVE;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Direction baseDir = this.getDirection();
	if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE || laserType == ArrowTypeConstants.LASER_TYPE_POWER) {
	    // Kill
	    final GameManager gm = DungeonDiver7.getApplication().getGameManager();
	    final DeadArrowTurret dat = new DeadArrowTurret();
	    dat.setSavedObject(this.getSavedObject());
	    dat.setDirection(baseDir);
	    gm.morph(dat, locX, locY, locZ, this.getLayer());
	    SoundLoader.playSound(SoundConstants.ANTI_DIE);
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_STUNNER) {
	    // Stun
	    final GameManager gm = DungeonDiver7.getApplication().getGameManager();
	    final StunnedArrowTurret sat = new StunnedArrowTurret();
	    sat.setSavedObject(this.getSavedObject());
	    sat.setDirection(baseDir);
	    gm.morph(sat, locX, locY, locZ, this.getLayer());
	    SoundLoader.playSound(SoundConstants.STUN);
	    return Direction.NONE;
	} else {
	    final Direction sourceDir = DirectionResolver.resolveRelativeDirectionInvert(dirX, dirY);
	    if (sourceDir == baseDir) {
		// Kill
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
		final DeadArrowTurret dat = new DeadArrowTurret();
		dat.setSavedObject(this.getSavedObject());
		dat.setDirection(baseDir);
		gm.morph(dat, locX, locY, locZ, this.getLayer());
		SoundLoader.playSound(SoundConstants.ANTI_DIE);
		return Direction.NONE;
	    } else {
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	if (this.getSavedObject().isOfType(TypeConstants.TYPE_ANTI_MOVER)) {
	    final Direction moveDir = this.getSavedObject().getDirection();
	    final int[] unres = DirectionResolver.unresolveRelativeDirection(moveDir);
	    if (GameManager.canObjectMove(locX, locY, unres[0], unres[1])) {
		if (this.autoMove) {
		    this.autoMove = false;
		    DungeonDiver7.getApplication().getGameManager().updatePushedPosition(locX, locY, locX + unres[0],
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
