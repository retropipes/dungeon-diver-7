/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractTransientObject;
import org.retropipes.dungeondiver7.dungeon.objects.Arrow;
import org.retropipes.dungeondiver7.dungeon.objects.ArrowTurretDisguise;
import org.retropipes.dungeondiver7.dungeon.objects.DeathArrow;
import org.retropipes.dungeondiver7.dungeon.objects.Disruptor;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.dungeon.objects.Ground;
import org.retropipes.dungeondiver7.dungeon.objects.InverseArrow;
import org.retropipes.dungeondiver7.dungeon.objects.Missile;
import org.retropipes.dungeondiver7.dungeon.objects.PowerArrow;
import org.retropipes.dungeondiver7.dungeon.objects.PowerfulParty;
import org.retropipes.dungeondiver7.dungeon.objects.Stunner;
import org.retropipes.dungeondiver7.dungeon.objects.Wall;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.PartyInventory;
import org.retropipes.dungeondiver7.utility.ShotTypes;

final class MovingLaserTracker {
	private static boolean canMoveThere(final int sx, final int sy) {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		final var plMgr = gm.getPlayerManager();
		final var px = plMgr.getPlayerLocationX();
		final var py = plMgr.getPlayerLocationY();
		final var pz = plMgr.getPlayerLocationZ();
		final var app = DungeonDiver7.getStuffBag();
		final var m = app.getDungeonManager().getDungeon();
		final var zproceed = true;
		AbstractDungeonObject zo = null;
		try {
			zo = m.getCell(px + sx, py + sy, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
		} catch (final ArrayIndexOutOfBoundsException ae) {
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
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		// Check cheats
		if (gm.getCheatStatus(GameLogic.CHEAT_GHOSTLY)) {
			return true;
		}
		final var nextSolid = next.isConditionallySolid();
		if (!nextSolid || next.isOfType(DungeonObjectTypes.TYPE_CHARACTER)) {
			return true;
		}
		return false;
	}

	private static AbstractTransientObject createLaserForType(final int type) {
		return switch (type) {
		case ShotTypes.GREEN -> new Arrow();
		case ShotTypes.BLUE -> new InverseArrow();
		case ShotTypes.RED -> new DeathArrow();
		case ShotTypes.MISSILE -> new Missile();
		case ShotTypes.STUNNER -> new Stunner();
		case ShotTypes.DISRUPTOR -> new Disruptor();
		case ShotTypes.POWER -> new PowerArrow();
		default -> null;
		};
	}

	private static int normalizeColumn(final int column, final int columns) {
		var fC = column;
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

	private static int normalizeRow(final int row, final int rows) {
		var fR = row;
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

	void activateLasers(final int zx, final int zy, final int zox, final int zoy, final int zlt,
			final AbstractDungeonObject zshooter) {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		this.shooter = zshooter;
		this.ox = zox;
		this.oy = zoy;
		this.lt = zlt;
		this.cumX = zx;
		this.cumY = zy;
		this.incX = zx;
		this.incY = zy;
		switch (this.lt) {
		case ShotTypes.GREEN:
			if (this.shooter instanceof PowerfulParty) {
				this.lt = ShotTypes.POWER;
				SoundLoader.playSound(Sounds.POWER_LASER);
			} else if (this.shooter instanceof ArrowTurretDisguise) {
				this.lt = ShotTypes.RED;
				SoundLoader.playSound(Sounds.ANTI_FIRE);
			} else {
				SoundLoader.playSound(Sounds.FIRE_LASER);
			}
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
			GameLogic.updateUndo(true, false, false, false, false, false, false, false, false, false);
			gm.updateScore(0, 1, 0);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
			break;
		case ShotTypes.RED:
			if (!gm.getCheatStatus(GameLogic.CHEAT_INVINCIBLE)) {
				SoundLoader.playSound(Sounds.ANTI_FIRE);
				this.laser = true;
				this.res = true;
			}
			break;
		case ShotTypes.MISSILE:
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
			GameLogic.updateUndo(false, true, false, false, false, false, false, false, false, false);
			PartyInventory.fireMissile();
			SoundLoader.playSound(Sounds.MISSILE);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
			break;
		case ShotTypes.STUNNER:
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
			GameLogic.updateUndo(false, false, true, false, false, false, false, false, false, false);
			PartyInventory.fireStunner();
			SoundLoader.playSound(Sounds.STUNNER);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
			break;
		case ShotTypes.BLUE:
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
			GameLogic.updateUndo(false, false, false, false, false, true, false, false, false, false);
			PartyInventory.fireBlueLaser();
			SoundLoader.playSound(Sounds.FIRE_LASER);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
			break;
		case ShotTypes.DISRUPTOR:
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
			GameLogic.updateUndo(false, false, false, false, false, false, true, false, false, false);
			PartyInventory.fireDisruptor();
			SoundLoader.playSound(Sounds.DISRUPTOR);
			gm.updateScore(0, 0, 1);
			if (!gm.isReplaying()) {
				gm.updateReplay(true, 0, 0);
			}
			this.laser = true;
			this.res = true;
			break;
		default:
			break;
		}
	}

	void clearLastLaser() {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		final var plMgr = gm.getPlayerManager();
		final var pz = plMgr.getPlayerLocationZ();
		if (this.laser) {
			// Clear last laser
			try {
				DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().setVirtualCell(new Empty(),
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
		final var g = new Ground();
		final var app = DungeonDiver7.getStuffBag();
		final var gm = app.getGameLogic();
		final var plMgr = app.getGameLogic().getPlayerManager();
		final var px = plMgr.getPlayerLocationX();
		final var py = plMgr.getPlayerLocationY();
		final var pz = plMgr.getPlayerLocationZ();
		final var m = app.getDungeonManager().getDungeon();
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
			if (this.lt == ShotTypes.MISSILE || this.lt != ShotTypes.STUNNER && this.lt != ShotTypes.DISRUPTOR) {
				final var suffix = DirectionResolver.resolve(this.incX, this.incY);
				this.l.setDirection(suffix);
			} else {
				// Do nothing
			}
			final var oldincX = this.incX;
			final var oldincY = this.incY;
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
			final var oldLaserDir = this.l.getDirection();
			laserDir = oldLaserDir;
			final var laserKill = this.ox + this.cumX == px && this.oy + this.cumY == py;
			if (laserKill) {
				gm.gameOver();
				return;
			}
			var dir = lou.laserEnteredAction(this.ox + this.cumX, this.oy + this.cumY, pz, this.incX, this.incY,
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
			resolved = DirectionResolver.unresolve(dir);
			var resX = resolved[0];
			var resY = resolved[1];
			laserDir = DirectionResolver.resolve(resX, resY);
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
			resolved = DirectionResolver.unresolve(dir);
			resX = resolved[0];
			resY = resolved[1];
			laserDir = DirectionResolver.resolve(resX, resY);
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

	boolean isChecking() {
		return this.res;
	}

	boolean isTracking() {
		return this.laser;
	}

	void trackPart1(final boolean tracking) {
		if (this.laser && this.res) {
			this.doLasersOnce(tracking);
		}
	}

	boolean trackPart2(final int nsx, final int nsy, final boolean nMover) {
		final var gm = DungeonDiver7.getStuffBag().getGameLogic();
		var sx = nsx;
		var sy = nsy;
		var mover = nMover;
		if (!this.res && this.laser) {
			if (gm.getPlayer().getSavedObject().isOfType(DungeonObjectTypes.TYPE_MOVER)) {
				final var dir = gm.getPlayer().getSavedObject().getDirection();
				final var unres = DirectionResolver.unresolve(dir);
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
}
