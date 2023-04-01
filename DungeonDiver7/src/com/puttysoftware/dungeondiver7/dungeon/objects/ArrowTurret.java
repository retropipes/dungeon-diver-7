/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.DirectionResolver;
import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.GameActions;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

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
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		final var baseDir = this.getDirection();
		if (laserType == ShotTypes.MISSILE || laserType == ShotTypes.POWER) {
			final var dat = new DeadArrowTurret();
			dat.setSavedObject(this.getSavedObject());
			dat.setDirection(baseDir);
			GameLogic.morph(dat, locX, locY, locZ, this.getLayer());
			SoundLoader.playSound(Sounds.ANTI_DIE);
			return Direction.NONE;
		}
		if (laserType == ShotTypes.STUNNER) {
			final var sat = new StunnedArrowTurret();
			sat.setSavedObject(this.getSavedObject());
			sat.setDirection(baseDir);
			GameLogic.morph(sat, locX, locY, locZ, this.getLayer());
			SoundLoader.playSound(Sounds.STUN);
			return Direction.NONE;
		}
		final var sourceDir = DirectionResolver.resolveInvert(dirX, dirY);
		if (sourceDir == baseDir) {
			final var dat = new DeadArrowTurret();
			dat.setSavedObject(this.getSavedObject());
			dat.setDirection(baseDir);
			GameLogic.morph(dat, locX, locY, locZ, this.getLayer());
			SoundLoader.playSound(Sounds.ANTI_DIE);
			return Direction.NONE;
		}
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}

	@Override
	public void timerExpiredAction(final int locX, final int locY) {
		if (this.getSavedObject().isOfType(DungeonObjectTypes.TYPE_ANTI_MOVER)) {
			final var moveDir = this.getSavedObject().getDirection();
			final var unres = DirectionResolver.unresolve(moveDir);
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
		SoundLoader.playSound(Sounds.PUSH_ANTI_TANK);
	}
}
