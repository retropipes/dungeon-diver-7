/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractTransientObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.ArrowTurretDisguise;
import com.puttysoftware.dungeondiver7.dungeon.objects.InverseArrow;
import com.puttysoftware.dungeondiver7.dungeon.objects.Disruptor;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Arrow;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.dungeon.objects.Missile;
import com.puttysoftware.dungeondiver7.dungeon.objects.PowerArrow;
import com.puttysoftware.dungeondiver7.dungeon.objects.PowerfulParty;
import com.puttysoftware.dungeondiver7.dungeon.objects.DeathArrow;
import com.puttysoftware.dungeondiver7.dungeon.objects.Stunner;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.PartyInventory;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

final class MovingLaserTracker {
	// Fields
	private AbstractDungeonObject shooter;
	private int ox, oy, lt;
	private boolean res;
	private boolean laser;
	private int cumX, cumY, incX, incY;
	private AbstractTransientObject l;

	// Constructors
	public MovingLaserTracker() {
		this.lt = 0;
	}

	boolean isTracking() {
		return this.laser;
	}

	boolean isChecking() {
		return this.res;
	}

	void activateLasers(final int zx, final int zy, final int zox, final int zoy, final int zlt,
			final AbstractDungeonObject zshooter) {
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
		this.shooter = zshooter;
		this.ox = zox;
		this.oy = zoy;
		this.lt = zlt;
		this.cumX = zx;
		this.cumY = zy;
		this.incX = zx;
		this.incY = zy;
		if (this.lt == ArrowTypeConstants.LASER_TYPE_GREEN) {
			if (this.shooter instanceof PowerfulParty) {
				this.lt = ArrowTypeConstants.LASER_TYPE_POWER;
				SoundLoader.playSound(SoundConstants.SOUND_POWER_LASER);
			} else if (this.shooter instanceof ArrowTurretDisguise) {
				this.lt = ArrowTypeConstants.LASER_TYPE_RED;
				SoundLoader.playSound(SoundConstants.SOUND_ANTI_FIRE);
			} else {
				SoundLoader.playSound(SoundConstants.SOUND_FIRE_LASER);
			}
			DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
			GameManager.updateUndo(true, false, false, false, false, false, false, false, false, false);
			gm.updateScore(0, 1, 0);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == ArrowTypeConstants.LASER_TYPE_RED) {
			if (!gm.getCheatStatus(GameManager.CHEAT_INVINCIBLE)) {
				SoundLoader.playSound(SoundConstants.SOUND_ANTI_FIRE);
				this.laser = true;
				this.res = true;
			}
		} else if (this.lt == ArrowTypeConstants.LASER_TYPE_MISSILE) {
			DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
			GameManager.updateUndo(false, true, false, false, false, false, false, false, false, false);
			PartyInventory.fireMissile();
			SoundLoader.playSound(SoundConstants.SOUND_MISSILE);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == ArrowTypeConstants.LASER_TYPE_STUNNER) {
			DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
			GameManager.updateUndo(false, false, true, false, false, false, false, false, false, false);
			PartyInventory.fireStunner();
			SoundLoader.playSound(SoundConstants.SOUND_STUNNER);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == ArrowTypeConstants.LASER_TYPE_BLUE) {
			DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
			GameManager.updateUndo(false, false, false, false, false, true, false, false, false, false);
			PartyInventory.fireBlueLaser();
			SoundLoader.playSound(SoundConstants.SOUND_FIRE_LASER);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
		} else if (this.lt == ArrowTypeConstants.LASER_TYPE_DISRUPTOR) {
			DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
			GameManager.updateUndo(false, false, false, false, false, false, true, false, false, false);
			PartyInventory.fireDisruptor();
			SoundLoader.playSound(SoundConstants.SOUND_DISRUPTOR);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
		}
	}

	void trackPart1(final boolean tracking) {
		if (this.laser && this.res) {
			this.doLasersOnce(tracking);
		}
	}

	boolean trackPart2(final int nsx, final int nsy, final boolean nMover) {
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
		int sx = nsx;
		int sy = nsy;
		boolean mover = nMover;
		if (!this.res && this.laser) {
			if (gm.getTank().getSavedObject().isOfType(TypeConstants.TYPE_MOVER)) {
				final Direction dir = gm.getTank().getSavedObject().getDirection();
				final int[] unres = DirectionResolver.unresolveRelativeDirection(dir);
				sx = unres[0];
				sy = unres[1];
				mover = true;
			}
			if (mover && !MovingLaserTracker.canMoveThere(sx, sy)) {
				MLOTask.activateAutomaticMovement();
			}
			this.clearLastLaser();
		}
		return mover;
	}

	void clearLastLaser() {
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
		final PlayerLocationManager plMgr = gm.getPlayerManager();
		final int pz = plMgr.getPlayerLocationZ();
		if (this.laser) {
			// Clear last laser
			try {
				DungeonDiver7.getApplication().getDungeonManager().getDungeon().setVirtualCell(new Empty(),
						this.ox + this.cumX - this.incX, this.oy + this.cumY - this.incY, pz, this.l.getLayer());
				gm.redrawDungeon();
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			}
			gm.laserDone();
			if (this.shooter.canShoot()) {
				this.shooter.laserDoneAction();
			}
			this.laser = false;
		}
	}

	private void doLasersOnce(final boolean tracking) {
		final Ground g = new Ground();
		final Application app = DungeonDiver7.getApplication();
		final GameManager gm = app.getGameManager();
		final PlayerLocationManager plMgr = app.getGameManager().getPlayerManager();
		final int px = plMgr.getPlayerLocationX();
		final int py = plMgr.getPlayerLocationY();
		final int pz = plMgr.getPlayerLocationZ();
		final AbstractDungeon m = app.getDungeonManager().getDungeon();
		AbstractDungeonObject lol = null;
		AbstractDungeonObject lou = null;
		try {
			lol = m.getCell(this.ox + this.cumX, this.oy + this.cumY, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
			lou = m.getCell(this.ox + this.cumX, this.oy + this.cumY, pz, DungeonConstants.LAYER_UPPER_OBJECTS);
		} catch (final ArrayIndexOutOfBoundsException ae) {
			this.res = false;
			lol = g;
			lou = g;
		}
		if (this.res) {
			int[] resolved;
			Direction laserDir;
			this.l = MovingLaserTracker.createLaserForType(this.lt);
			if (this.lt == ArrowTypeConstants.LASER_TYPE_MISSILE) {
				final Direction suffix = DirectionResolver.resolveRelativeDirection(this.incX, this.incY);
				this.l.setDirection(suffix);
			} else if (this.lt == ArrowTypeConstants.LASER_TYPE_STUNNER
					|| this.lt == ArrowTypeConstants.LASER_TYPE_DISRUPTOR) {
				// Do nothing
			} else {
				final Direction suffix = DirectionResolver.resolveRelativeDirectionHV(this.incX, this.incY);
				this.l.setDirection(suffix);
			}
			final int oldincX = this.incX;
			final int oldincY = this.incY;
			try {
				if (lol.doLasersPassThrough() && lou.doLasersPassThrough()) {
					m.setVirtualCell(this.l, this.ox + this.cumX, this.oy + this.cumY, pz, this.l.getLayer());
				}
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			}
			try {
				m.setVirtualCell(new Empty(), this.ox + this.cumX - this.incX, this.oy + this.cumY - this.incY, pz,
						this.l.getLayer());
			} catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			}
			final Direction oldLaserDir = this.l.getDirection();
			laserDir = oldLaserDir;
			final boolean laserKill = this.ox + this.cumX == px && this.oy + this.cumY == py;
			if (laserKill) {
				gm.gameOver();
				return;
			}
			Direction dir = lou.laserEnteredAction(this.ox + this.cumX, this.oy + this.cumY, pz, this.incX, this.incY,
					this.lt, this.l.getForceUnitsImbued());
			if (dir != Direction.NONE) {
				dir = lol.laserEnteredAction(this.ox + this.cumX, this.oy + this.cumY, pz, this.incX, this.incY,
						this.lt, this.l.getForceUnitsImbued());
			}
			if (dir == Direction.NONE) {
				this.res = false;
				// Clear laser, because it died
				try {
					m.setVirtualCell(new Empty(), this.ox + this.cumX, this.oy + this.cumY, pz, this.l.getLayer());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Ignore
				}
				return;
			}
			resolved = DirectionResolver.unresolveRelativeDirection(dir);
			int resX = resolved[0];
			int resY = resolved[1];
			laserDir = DirectionResolver.resolveRelativeDirectionHV(resX, resY);
			this.l.setDirection(laserDir);
			this.incX = resX;
			this.incY = resY;
			dir = lou.laserExitedAction(oldincX, oldincY, pz, this.incX, this.incY, this.lt);
			if (dir != Direction.NONE) {
				dir = lol.laserExitedAction(oldincX, oldincY, pz, this.incX, this.incY, this.lt);
			}
			if (dir == Direction.NONE) {
				this.res = false;
				// Clear laser, because it died
				try {
					m.setVirtualCell(new Empty(), this.ox + this.cumX, this.oy + this.cumY, pz, this.l.getLayer());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Ignore
				}
				return;
			}
			resolved = DirectionResolver.unresolveRelativeDirection(dir);
			resX = resolved[0];
			resY = resolved[1];
			laserDir = DirectionResolver.resolveRelativeDirectionHV(resX, resY);
			this.l.setDirection(laserDir);
			this.incX = resX;
			this.incY = resY;
			if (m.isVerticalWraparoundEnabled()) {
				this.cumX = MovingLaserTracker.normalizeColumn(this.cumX + this.incX, AbstractDungeon.getMinColumns());
			} else {
				this.cumX += this.incX;
			}
			if (m.isHorizontalWraparoundEnabled()) {
				this.cumY = MovingLaserTracker.normalizeRow(this.cumY + this.incY, AbstractDungeon.getMinRows());
			} else {
				this.cumY += this.incY;
			}
			if (oldLaserDir != laserDir && tracking) {
				try {
					m.setVirtualCell(new Empty(), this.ox + this.cumX - this.incX, this.oy + this.cumY - this.incY, pz,
							this.l.getLayer());
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
					// Ignore
				}
				if (m.isVerticalWraparoundEnabled()) {
					this.cumX = MovingLaserTracker.normalizeColumn(this.cumX + this.incX,
							AbstractDungeon.getMinColumns());
				} else {
					this.cumX += this.incX;
				}
				if (m.isHorizontalWraparoundEnabled()) {
					this.cumY = MovingLaserTracker.normalizeColumn(this.cumY + this.incY,
							AbstractDungeon.getMinColumns());
				} else {
					this.cumY += this.incY;
				}
			}
		}
		gm.redrawDungeon();
	}

	private static AbstractTransientObject createLaserForType(final int type) {
		switch (type) {
		case ArrowTypeConstants.LASER_TYPE_GREEN:
			return new Arrow();
		case ArrowTypeConstants.LASER_TYPE_BLUE:
			return new InverseArrow();
		case ArrowTypeConstants.LASER_TYPE_RED:
			return new DeathArrow();
		case ArrowTypeConstants.LASER_TYPE_MISSILE:
			return new Missile();
		case ArrowTypeConstants.LASER_TYPE_STUNNER:
			return new Stunner();
		case ArrowTypeConstants.LASER_TYPE_DISRUPTOR:
			return new Disruptor();
		case ArrowTypeConstants.LASER_TYPE_POWER:
			return new PowerArrow();
		default:
			return null;
		}
	}

	private static int normalizeRow(final int row, final int rows) {
		int fR = row;
		if (fR < 0) {
			fR += rows;
			while (fR < 0) {
				fR += rows;
			}
		} else if (fR > rows - 1) {
			fR -= rows;
			while (fR > rows - 1) {
				fR -= rows;
			}
		}
		return fR;
	}

	private static int normalizeColumn(final int column, final int columns) {
		int fC = column;
		if (fC < 0) {
			fC += columns;
			while (fC < 0) {
				fC += columns;
			}
		} else if (fC > columns - 1) {
			fC -= columns;
			while (fC > columns - 1) {
				fC -= columns;
			}
		}
		return fC;
	}

	private static boolean canMoveThere(final int sx, final int sy) {
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
		final PlayerLocationManager plMgr = gm.getPlayerManager();
		final int px = plMgr.getPlayerLocationX();
		final int py = plMgr.getPlayerLocationY();
		final int pz = plMgr.getPlayerLocationZ();
		final Application app = DungeonDiver7.getApplication();
		final AbstractDungeon m = app.getDungeonManager().getDungeon();
		boolean zproceed = true;
		AbstractDungeonObject zo = null;
		try {
			try {
				zo = m.getCell(px + sx, py + sy, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
			} catch (final ArrayIndexOutOfBoundsException ae) {
				zo = new Wall();
			}
		} catch (final NullPointerException np) {
			zproceed = false;
			zo = new Wall();
		}
		if (zproceed) {
			try {
				if (MovingLaserTracker.checkSolid(zo)) {
					return true;
				}
			} catch (final ArrayIndexOutOfBoundsException ae) {
				// Ignore
			}
		}
		return false;
	}

	private static boolean checkSolid(final AbstractDungeonObject next) {
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
		// Check cheats
		if (gm.getCheatStatus(GameManager.CHEAT_GHOSTLY)) {
			return true;
		} else {
			final boolean nextSolid = next.isConditionallySolid();
			if (nextSolid) {
				if (next.isOfType(TypeConstants.TYPE_CHARACTER)) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
	}
}
