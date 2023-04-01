/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.puttysoftware.diane.direction.DirectionResolver;
import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeonData;
import com.puttysoftware.dungeondiver7.dungeon.objects.FrozenParty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.dungeon.objects.Party;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.prefs.Prefs;
import com.puttysoftware.dungeondiver7.utility.AlreadyDeadException;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.GameActions;

final class MLOTask extends Thread {
    // Fields
    private int sx, sy;
    private boolean mover;
    private boolean move;
    private boolean proceed;
    private boolean abort;
    private boolean frozen;
    private boolean magnet;
    private boolean loopCheck;
    private final ArrayList<MovingLaserTracker> laserTrackers;
    private final ArrayList<MovingObjectTracker> objectTrackers;

    // Constructors
    public MLOTask() {
	this.setName(Strings.untranslated(Untranslated.MLOH_NAME));
	this.setPriority(Thread.MIN_PRIORITY);
	this.abort = false;
	this.laserTrackers = new ArrayList<>();
	this.objectTrackers = new ArrayList<>();
	this.frozen = false;
	this.magnet = false;
    }

    @Override
    public void run() {
	try {
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    gm.clearDead();
	    this.doMovementLasersObjects();
	    // Check auto-move flag
	    if (gm.isAutoMoveScheduled() && this.canMoveThere()) {
		gm.unscheduleAutoMove();
		this.doMovementLasersObjects();
	    }
	} catch (final AlreadyDeadException ade) {
	    // Ignore
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }

    void abortLoop() {
	this.abort = true;
    }

    void activateMovement(final int zx, final int zy) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	if (zx == 2 || zy == 2 || zx == -2 || zy == -2) {
	    // Boosting
	    SoundLoader.playSound(Sounds.BOOST);
	    gm.updateScore(0, 0, 1);
	    this.sx = zx;
	    this.sy = zy;
	    this.magnet = false;
	    GameLogic.updateUndo(false, false, false, true, false, false, false, false, false, false);
	} else if (zx == 3 || zy == 3 || zx == -3 || zy == -3) {
	    // Using a Magnet
	    gm.updateScore(0, 0, 1);
	    final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	    final var px = gm.getPlayerManager().getPlayerLocationX();
	    final var py = gm.getPlayerManager().getPlayerLocationY();
	    final var pz = gm.getPlayerManager().getPlayerLocationZ();
	    if (zx == 3) {
		this.sx = a.checkForMagnetic(pz, px, py, Direction.EAST);
		this.sy = 0;
	    } else if (zx == -3) {
		this.sx = -a.checkForMagnetic(pz, px, py, Direction.WEST);
		this.sy = 0;
	    }
	    if (zy == 3) {
		this.sx = 0;
		this.sy = a.checkForMagnetic(pz, px, py, Direction.SOUTH);
	    } else if (zy == -3) {
		this.sx = 0;
		this.sy = -a.checkForMagnetic(pz, px, py, Direction.NORTH);
	    }
	    this.magnet = true;
	    if (this.sx == 0 && this.sy == 0) {
		// Failure
		SoundLoader.playSound(Sounds.BUMP_HEAD);
	    } else {
		// Success
		SoundLoader.playSound(Sounds.MAGNET);
	    }
	    GameLogic.updateUndo(false, false, false, false, true, false, false, false, false, false);
	} else {
	    // Moving normally
	    SoundLoader.playSound(Sounds.MOVE);
	    gm.updateScore(1, 0, 0);
	    this.sx = zx;
	    this.sy = zy;
	    this.magnet = false;
	    GameLogic.updateUndo(false, false, false, false, false, false, false, false, false, false);
	}
	this.move = true;
	this.loopCheck = true;
	if (!gm.isReplaying()) {
	    gm.updateReplay(false, zx, zy);
	}
    }

    void activateFrozenMovement(final int zx, final int zy) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	// Moving under the influence of a Frost Field
	this.frozen = true;
	MLOTask.freezePlayer();
	gm.updateScore(1, 0, 0);
	this.sx = zx;
	this.sy = zy;
	GameLogic.updateUndo(false, false, false, false, false, false, false, false, false, false);
	this.move = true;
	if (!gm.isReplaying()) {
	    gm.updateReplay(false, zx, zy);
	}
    }

    static void activateAutomaticMovement() {
	DungeonDiver7.getStuffBag().getGameLogic().scheduleAutoMove();
    }

    void activateObjects(final int zx, final int zy, final int pushX, final int pushY,
	    final AbstractMovableObject gmo) {
	final var tracker = new MovingObjectTracker();
	tracker.activateObject(zx, zy, pushX, pushY, gmo);
	this.objectTrackers.add(tracker);
    }

    void haltMovingObjects() {
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isTracking()) {
		tracker.haltMovingObject();
	    }
	}
    }

    void activateLasers(final int zx, final int zy, final int zox, final int zoy, final int zlt,
	    final AbstractDungeonObject zshooter) {
	final var tracker = new MovingLaserTracker();
	tracker.activateLasers(zx, zy, zox, zoy, zlt, zshooter);
	this.laserTrackers.add(tracker);
    }

    private void doMovementLasersObjects() {
	synchronized (CurrentDungeonData.LOCK_OBJECT) {
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    final var plMgr = gm.getPlayerManager();
	    final var pz = plMgr.getPlayerLocationZ();
	    this.loopCheck = true;
	    var objs = new AbstractDungeonObject[4];
	    objs[DungeonConstants.LAYER_LOWER_GROUND] = new Wall();
	    objs[DungeonConstants.LAYER_UPPER_GROUND] = new Wall();
	    objs[DungeonConstants.LAYER_LOWER_OBJECTS] = new Wall();
	    objs[DungeonConstants.LAYER_UPPER_OBJECTS] = new Wall();
	    do {
		try {
		    if (this.move && this.loopCheck) {
			objs = this.doMovementOnce();
		    }
		    // Abort check 1
		    if (this.abort) {
			break;
		    }
		    for (final MovingLaserTracker tracker : this.laserTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart1(this.areObjectTrackersTracking());
			}
		    }
		    // Abort check 2
		    if (this.abort) {
			break;
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart1();
			}
		    }
		    // Abort check 3
		    if (this.abort) {
			break;
		    }
		    var actionType = 0;
		    if (this.move && !this.magnet && Math.abs(this.sx) <= 1 && Math.abs(this.sy) <= 1) {
			actionType = GameActions.MOVE;
		    } else {
			actionType = GameActions.NON_MOVE;
		    }
		    for (final MovingLaserTracker tracker : this.laserTrackers) {
			if (tracker.isTracking()) {
			    this.mover = tracker.trackPart2(this.sx, this.sy, this.mover);
			}
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart2();
			}
		    }
		    if (this.move) {
			this.loopCheck = this.checkLoopCondition(this.proceed);
			if (this.mover && !this.canMoveThere()) {
			    MLOTask.activateAutomaticMovement();
			}
			if (objs[DungeonConstants.LAYER_LOWER_OBJECTS].solvesOnMove()) {
			    this.abort = true;
			    if (this.move) {
				DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
				this.defrostPlayer();
				gm.moveLoopDone();
				this.move = false;
			    }
			    for (final MovingLaserTracker tracker : this.laserTrackers) {
				if (tracker.isTracking()) {
				    tracker.clearLastLaser();
				}
			    }
			    gm.solvedLevel(true);
			    return;
			}
		    } else {
			this.loopCheck = false;
		    }
		    if (this.move && !this.loopCheck) {
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
			this.defrostPlayer();
			gm.moveLoopDone();
			this.move = false;
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart3();
			}
		    }
		    // Check auto-move flag
		    if (gm.isAutoMoveScheduled() && this.canMoveThere()) {
			gm.unscheduleAutoMove();
			this.move = true;
			this.loopCheck = true;
		    }
		    DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().tickTimers(pz, actionType);
		    final var px = plMgr.getPlayerLocationX();
		    final var py = plMgr.getPlayerLocationY();
		    DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().checkForEnemies(pz, px, py,
			    DungeonDiver7.getStuffBag().getGameLogic().getPlayer());
		    // Delay
		    try {
			Thread.sleep(Prefs.getActionSpeed());
		    } catch (final InterruptedException ie) {
			// Ignore
		    }
		    for (final MovingObjectTracker tracker : this.objectTrackers) {
			if (tracker.isTracking()) {
			    tracker.trackPart4();
			}
		    }
		    this.cullTrackers();
		} catch (final ConcurrentModificationException cme) {
		    // Ignore
		}
	    } while (!this.abort
		    && (this.loopCheck || this.areLaserTrackersChecking() || this.areObjectTrackersChecking()));
	    // Check cheats
	    if (objs[DungeonConstants.LAYER_LOWER_GROUND].killsOnMove()
		    && !gm.getCheatStatus(GameLogic.CHEAT_SWIMMING)) {
		gm.gameOver();
	    }
	}
    }

    private boolean canMoveThere() {
	final var app = DungeonDiver7.getStuffBag();
	final var gm = app.getGameLogic();
	final var plMgr = gm.getPlayerManager();
	final var px = plMgr.getPlayerLocationX();
	final var py = plMgr.getPlayerLocationY();
	final var pz = plMgr.getPlayerLocationZ();
	final var pw = DungeonConstants.LAYER_UPPER_OBJECTS;
	final var m = app.getDungeonManager().getDungeon();
	AbstractDungeonObject lgo = null;
	AbstractDungeonObject ugo = null;
	AbstractDungeonObject loo = null;
	AbstractDungeonObject uoo = null;
	try {
	    lgo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    lgo = new Wall();
	}
	try {
	    ugo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_UPPER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    ugo = new Wall();
	}
	try {
	    loo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    loo = new Wall();
	}
	try {
	    uoo = m.getCell(px + this.sx, py + this.sy, pz, pw);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    uoo = new Wall();
	}
	return MLOTask.checkSolid(lgo) && MLOTask.checkSolid(ugo) && MLOTask.checkSolid(loo) && MLOTask.checkSolid(uoo);
    }

    private AbstractDungeonObject[] doMovementOnce() {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	final var plMgr = gm.getPlayerManager();
	var px = plMgr.getPlayerLocationX();
	var py = plMgr.getPlayerLocationY();
	final var pz = plMgr.getPlayerLocationZ();
	final var pw = DungeonConstants.LAYER_UPPER_OBJECTS;
	final var app = DungeonDiver7.getStuffBag();
	final var m = app.getDungeonManager().getDungeon();
	this.proceed = true;
	this.mover = false;
	AbstractDungeonObject lgo = null;
	AbstractDungeonObject ugo = null;
	AbstractDungeonObject loo = null;
	AbstractDungeonObject uoo = null;
	try {
	    lgo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    lgo = new Wall();
	}
	try {
	    ugo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_UPPER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    ugo = new Wall();
	}
	try {
	    loo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    loo = new Wall();
	}
	try {
	    uoo = m.getCell(px + this.sx, py + this.sy, pz, pw);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    uoo = new Wall();
	}
	if (this.proceed) {
	    plMgr.savePlayerLocation();
	    try {
		if (this.canMoveThere()) {
		    if (gm.isDelayedDecayActive()) {
			gm.doDelayedDecay();
		    }
		    // Preserve other objects
		    if (m.getCell(px, py, pz, pw) instanceof AbstractMovableObject) {
			gm.getPlayer().setSavedObject(m.getCell(px, py, pz, pw));
		    }
		    m.setCell(gm.getPlayer().getSavedObject(), px, py, pz, pw);
		    plMgr.offsetPlayerLocationX(this.sx);
		    plMgr.offsetPlayerLocationY(this.sy);
		    px = MLOTask.normalizeColumn(px + this.sx, AbstractDungeon.getMinColumns());
		    py = MLOTask.normalizeRow(py + this.sy, AbstractDungeon.getMinRows());
		    gm.getPlayer().setSavedObject(m.getCell(px, py, pz, pw));
		    m.setCell(gm.getPlayer(), px, py, pz, pw);
		    lgo.postMoveAction(px, py, pz);
		    ugo.postMoveAction(px, py, pz);
		    loo.postMoveAction(px, py, pz);
		    uoo.postMoveAction(px, py, pz);
		    if (ugo.isOfType(DungeonObjectTypes.TYPE_MOVER)) {
			final var dir = ugo.getDirection();
			final var unres = DirectionResolver.unresolve(dir);
			this.sx = unres[0];
			this.sy = unres[1];
			this.mover = true;
		    } else {
			this.mover = false;
		    }
		} else {
		    // Move failed - object is solid in that direction
		    if (gm.isDelayedDecayActive()) {
			gm.doDelayedDecay();
		    }
		    if (lgo == null) {
			lgo = new Ground();
		    }
		    lgo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			    plMgr.getPlayerLocationZ());
		    if (ugo == null) {
			ugo = new Ground();
		    }
		    ugo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			    plMgr.getPlayerLocationZ());
		    if (loo == null) {
			loo = new Ground();
		    }
		    loo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			    plMgr.getPlayerLocationZ());
		    if (uoo == null) {
			uoo = new Ground();
		    }
		    uoo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			    plMgr.getPlayerLocationZ());
		    if (gm.getPlayer().getSavedObject().isOfType(DungeonObjectTypes.TYPE_MOVER)) {
			final var dir = gm.getPlayer().getSavedObject().getDirection();
			final var unres = DirectionResolver.unresolve(dir);
			this.sx = unres[0];
			this.sy = unres[1];
			this.mover = true;
		    } else {
			this.mover = false;
		    }
		    this.proceed = false;
		}
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		plMgr.restorePlayerLocation();
		m.setCell(gm.getPlayer(), plMgr.getPlayerLocationX(), plMgr.getPlayerLocationY(),
			plMgr.getPlayerLocationZ(), pw);
		// Move failed - attempted to go outside the dungeon
		if (lgo == null) {
		    lgo = new Ground();
		}
		lgo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			plMgr.getPlayerLocationZ());
		if (ugo == null) {
		    ugo = new Ground();
		}
		ugo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			plMgr.getPlayerLocationZ());
		if (loo == null) {
		    loo = new Ground();
		}
		loo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			plMgr.getPlayerLocationZ());
		if (uoo == null) {
		    uoo = new Ground();
		}
		uoo.moveFailedAction(plMgr.getPlayerLocationX() + this.sx, plMgr.getPlayerLocationY() + this.sy,
			plMgr.getPlayerLocationZ());
		this.proceed = false;
	    }
	} else {
	    // Move failed - pre-move check failed
	    lgo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    ugo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    loo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    uoo.moveFailedAction(px + this.sx, py + this.sy, pz);
	    this.proceed = false;
	}
	gm.redrawDungeon();
	return new AbstractDungeonObject[] { lgo, ugo, loo, uoo };
    }

    private boolean checkLoopCondition(final boolean zproceed) {
	final var app = DungeonDiver7.getStuffBag();
	final var gm = app.getGameLogic();
	final var plMgr = gm.getPlayerManager();
	final var px = plMgr.getPlayerLocationX();
	final var py = plMgr.getPlayerLocationY();
	final var pz = plMgr.getPlayerLocationZ();
	final var m = app.getDungeonManager().getDungeon();
	AbstractDungeonObject lgo = null;
	AbstractDungeonObject ugo = null;
	try {
	    lgo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    lgo = new Wall();
	}
	try {
	    ugo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_UPPER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    ugo = new Wall();
	}
	return zproceed && (!lgo.hasFriction() || !ugo.hasFriction() || this.mover || this.frozen)
		&& this.canMoveThere();
    }

    private static void freezePlayer() {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	final var tank = gm.getPlayer();
	final var dir = tank.getDirection();
	final var px = gm.getPlayerManager().getPlayerLocationX();
	final var py = gm.getPlayerManager().getPlayerLocationY();
	final var pz = gm.getPlayerManager().getPlayerLocationZ();
	final var ft = new FrozenParty(dir, tank.getNumber());
	ft.setSavedObject(tank.getSavedObject());
	GameLogic.morph(ft, px, py, pz, ft.getLayer());
	gm.updatePlayer();
    }

    private void defrostPlayer() {
	if (this.frozen) {
	    this.frozen = false;
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    final var tank = gm.getPlayer();
	    final var dir = tank.getDirection();
	    final var px = gm.getPlayerManager().getPlayerLocationX();
	    final var py = gm.getPlayerManager().getPlayerLocationY();
	    final var pz = gm.getPlayerManager().getPlayerLocationZ();
	    final var t = new Party(dir, tank.getNumber());
	    t.setSavedObject(tank.getSavedObject());
	    GameLogic.morph(t, px, py, pz, t.getLayer());
	    gm.updatePlayer();
	    SoundLoader.playSound(Sounds.DEFROST);
	}
    }

    private static boolean checkSolid(final AbstractDungeonObject next) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	// Check cheats
	if (gm.getCheatStatus(GameLogic.CHEAT_GHOSTLY)) {
	    return true;
	}
	return !next.isConditionallySolid();
    }

    static boolean checkSolid(final int zx, final int zy) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	final var next = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(zx, zy,
		gm.getPlayerManager().getPlayerLocationZ(), DungeonConstants.LAYER_LOWER_OBJECTS);
	// Check cheats
	if (gm.getCheatStatus(GameLogic.CHEAT_GHOSTLY)) {
	    return true;
	}
	return !next.isConditionallySolid();
    }

    private boolean areObjectTrackersTracking() {
	var result = false;
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isTracking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean areObjectTrackersChecking() {
	var result = false;
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isChecking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean areLaserTrackersChecking() {
	var result = false;
	for (final MovingLaserTracker tracker : this.laserTrackers) {
	    if (tracker.isChecking()) {
		result = true;
	    }
	}
	return result;
    }

    private void cullTrackers() {
	final var tempArray1 = this.objectTrackers.toArray(new MovingObjectTracker[this.objectTrackers.size()]);
	this.objectTrackers.clear();
	for (final MovingObjectTracker tracker : tempArray1) {
	    if (tracker != null && tracker.isTracking()) {
		this.objectTrackers.add(tracker);
	    }
	}
	final var tempArray2 = this.laserTrackers.toArray(new MovingLaserTracker[this.laserTrackers.size()]);
	this.laserTrackers.clear();
	for (final MovingLaserTracker tracker : tempArray2) {
	    if (tracker != null && tracker.isTracking()) {
		this.laserTrackers.add(tracker);
	    }
	}
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
}
