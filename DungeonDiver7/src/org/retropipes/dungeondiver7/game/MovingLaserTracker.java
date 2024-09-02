/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game;

import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractTransientObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.objects.Arrow;
import org.retropipes.dungeondiver7.dungeon.objects.ArrowTurretDisguise;
import org.retropipes.dungeondiver7.dungeon.objects.DeathArrow;
import org.retropipes.dungeondiver7.dungeon.objects.Disruptor;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.dungeon.objects.InverseArrow;
import org.retropipes.dungeondiver7.dungeon.objects.Missile;
import org.retropipes.dungeondiver7.dungeon.objects.PowerArrow;
import org.retropipes.dungeondiver7.dungeon.objects.PowerfulParty;
import org.retropipes.dungeondiver7.dungeon.objects.Stunner;
import org.retropipes.dungeondiver7.dungeon.objects.Wall;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
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
	DungeonObject zo = null;
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

    private static boolean checkSolid(final DungeonObject next) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	// Check cheats
	if (gm.getCheatStatus(GameLogic.CHEAT_GHOSTLY)) {
	    return true;
	}
	final var nextSolid = next.isConditionallySolid();
	if (!nextSolid || next.isPlayer()) {
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

    // Fields
    private DungeonObject shooter;
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
	    final DungeonObject zshooter) {
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
	    } else if (this.shooter instanceof ArrowTurretDisguise) {
		this.lt = ShotTypes.RED;
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
	    this.laser = false;
	}
    }

    private void doLasersOnce(final boolean tracking) {
	final var app = DungeonDiver7.getStuffBag();
	final var gm = app.getGameLogic();
	final var plMgr = app.getGameLogic().getPlayerManager();
	final var px = plMgr.getPlayerLocationX();
	final var py = plMgr.getPlayerLocationY();
	final var pz = plMgr.getPlayerLocationZ();
	final var m = app.getDungeonManager().getDungeon();
	if (this.res) {
	    this.l = MovingLaserTracker.createLaserForType(this.lt);
	    if (this.lt == ShotTypes.MISSILE || this.lt != ShotTypes.STUNNER && this.lt != ShotTypes.DISRUPTOR) {
		final var suffix = DirectionResolver.resolve(this.incX, this.incY);
		this.l.setDirection(suffix);
	    } else {
		// Do nothing
	    }
	    try {
		m.setVirtualCell(new Empty(), this.ox + this.cumX - this.incX, this.oy + this.cumY - this.incY, pz,
			this.l.getLayer());
	    } catch (final ArrayIndexOutOfBoundsException aioobe) {
		// Ignore
	    }
	    final var laserKill = this.ox + this.cumX == px && this.oy + this.cumY == py;
	    if (laserKill) {
		gm.gameOver();
		return;
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
	    if (gm.getPlayer().getSavedObject().canMoveParty()) {
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
