/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.puttysoftware.dungeondiver7.BagOStuff;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeonData;
import com.puttysoftware.dungeondiver7.dungeon.objects.FrozenParty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.dungeon.objects.Party;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.ActionConstants;
import com.puttysoftware.dungeondiver7.utility.AlreadyDeadException;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

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
	this.setName(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_MLOH_NAME));
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
	    final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
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
	    DungeonDiver7.getErrorLogger().logError(t);
	}
    }

    void abortLoop() {
	this.abort = true;
    }

    void activateMovement(final int zx, final int zy) {
	final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	if (zx == 2 || zy == 2 || zx == -2 || zy == -2) {
	    // Boosting
	    SoundLoader.playSound(SoundConstants.BOOST);
	    gm.updateScore(0, 0, 1);
	    this.sx = zx;
	    this.sy = zy;
	    this.magnet = false;
	    GameLogic.updateUndo(false, false, false, true, false, false, false, false, false, false);
	} else if (zx == 3 || zy == 3 || zx == -3 || zy == -3) {
	    // Using a Magnet
	    gm.updateScore(0, 0, 1);
	    final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	    final int px = gm.getPlayerManager().getPlayerLocationX();
	    final int py = gm.getPlayerManager().getPlayerLocationY();
	    final int pz = gm.getPlayerManager().getPlayerLocationZ();
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
		SoundLoader.playSound(SoundConstants.BUMP_HEAD);
	    } else {
		// Success
		SoundLoader.playSound(SoundConstants.MAGNET);
	    }
	    GameLogic.updateUndo(false, false, false, false, true, false, false, false, false, false);
	} else {
	    // Moving normally
	    SoundLoader.playSound(SoundConstants.MOVE);
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
	final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
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
	DungeonDiver7.getApplication().getGameLogic().scheduleAutoMove();
    }

    void activateObjects(final int zx, final int zy, final int pushX, final int pushY,
	    final AbstractMovableObject gmo) {
	final MovingObjectTracker tracker = new MovingObjectTracker();
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
	final MovingLaserTracker tracker = new MovingLaserTracker();
	tracker.activateLasers(zx, zy, zox, zoy, zlt, zshooter);
	this.laserTrackers.add(tracker);
    }

    private void doMovementLasersObjects() {
	synchronized (CurrentDungeonData.LOCK_OBJECT) {
	    final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	    final PlayerLocationManager plMgr = gm.getPlayerManager();
	    final int pz = plMgr.getPlayerLocationZ();
	    this.loopCheck = true;
	    AbstractDungeonObject[] objs = new AbstractDungeonObject[4];
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
		    int actionType = 0;
		    if (this.move) {
			if (!this.magnet && Math.abs(this.sx) <= 1 && Math.abs(this.sy) <= 1) {
			    actionType = ActionConstants.ACTION_MOVE;
			} else {
			    actionType = ActionConstants.ACTION_NON_MOVE;
			}
		    } else {
			actionType = ActionConstants.ACTION_NON_MOVE;
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
				DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
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
			DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
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
		    DungeonDiver7.getApplication().getDungeonManager().getDungeon().tickTimers(pz, actionType);
		    final int px = plMgr.getPlayerLocationX();
		    final int py = plMgr.getPlayerLocationY();
		    DungeonDiver7.getApplication().getDungeonManager().getDungeon().checkForEnemies(pz, px, py,
			    DungeonDiver7.getApplication().getGameLogic().getPlayer());
		    // Delay
		    try {
			Thread.sleep(PrefsManager.getActionSpeed());
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
	    if (objs[DungeonConstants.LAYER_LOWER_GROUND].killsOnMove()) {
		// Check cheats
		if (!gm.getCheatStatus(GameLogic.CHEAT_SWIMMING)) {
		    gm.gameOver();
		}
	    }
	}
    }

    private boolean canMoveThere() {
	final BagOStuff app = DungeonDiver7.getApplication();
	final GameLogic gm = app.getGameLogic();
	final PlayerLocationManager plMgr = gm.getPlayerManager();
	final int px = plMgr.getPlayerLocationX();
	final int py = plMgr.getPlayerLocationY();
	final int pz = plMgr.getPlayerLocationZ();
	final int pw = DungeonConstants.LAYER_UPPER_OBJECTS;
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	AbstractDungeonObject lgo = null;
	AbstractDungeonObject ugo = null;
	AbstractDungeonObject loo = null;
	AbstractDungeonObject uoo = null;
	try {
	    try {
		lgo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_GROUND);
		ugo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_UPPER_GROUND);
		loo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
		uoo = m.getCell(px + this.sx, py + this.sy, pz, pw);
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		lgo = new Wall();
		ugo = new Wall();
		loo = new Wall();
		uoo = new Wall();
	    }
	} catch (final NullPointerException np) {
	    this.proceed = false;
	    lgo = new Wall();
	    ugo = new Wall();
	    loo = new Wall();
	    uoo = new Wall();
	}
	return MLOTask.checkSolid(lgo) && MLOTask.checkSolid(ugo) && MLOTask.checkSolid(loo) && MLOTask.checkSolid(uoo);
    }

    private AbstractDungeonObject[] doMovementOnce() {
	final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	final PlayerLocationManager plMgr = gm.getPlayerManager();
	int px = plMgr.getPlayerLocationX();
	int py = plMgr.getPlayerLocationY();
	final int pz = plMgr.getPlayerLocationZ();
	final int pw = DungeonConstants.LAYER_UPPER_OBJECTS;
	final BagOStuff app = DungeonDiver7.getApplication();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	this.proceed = true;
	this.mover = false;
	AbstractDungeonObject lgo = null;
	AbstractDungeonObject ugo = null;
	AbstractDungeonObject loo = null;
	AbstractDungeonObject uoo = null;
	try {
	    try {
		lgo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_GROUND);
		ugo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_UPPER_GROUND);
		loo = m.getCell(px + this.sx, py + this.sy, pz, DungeonConstants.LAYER_LOWER_OBJECTS);
		uoo = m.getCell(px + this.sx, py + this.sy, pz, pw);
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		lgo = new Wall();
		ugo = new Wall();
		loo = new Wall();
		uoo = new Wall();
	    }
	} catch (final NullPointerException np) {
	    this.proceed = false;
	    lgo = new Wall();
	    ugo = new Wall();
	    loo = new Wall();
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
		    if (ugo.isOfType(TypeConstants.TYPE_MOVER)) {
			final Direction dir = ugo.getDirection();
			final int[] unres = DirectionResolver.unresolveRelativeDirection(dir);
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
		    if (gm.getPlayer().getSavedObject().isOfType(TypeConstants.TYPE_MOVER)) {
			final Direction dir = gm.getPlayer().getSavedObject().getDirection();
			final int[] unres = DirectionResolver.unresolveRelativeDirection(dir);
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
	final BagOStuff app = DungeonDiver7.getApplication();
	final GameLogic gm = app.getGameLogic();
	final PlayerLocationManager plMgr = gm.getPlayerManager();
	final int px = plMgr.getPlayerLocationX();
	final int py = plMgr.getPlayerLocationY();
	final int pz = plMgr.getPlayerLocationZ();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	AbstractDungeonObject lgo = null;
	AbstractDungeonObject ugo = null;
	try {
	    try {
		lgo = m.getCell(px, py, pz, DungeonConstants.LAYER_LOWER_GROUND);
		ugo = m.getCell(px, py, pz, DungeonConstants.LAYER_UPPER_GROUND);
	    } catch (final ArrayIndexOutOfBoundsException ae) {
		lgo = new Wall();
		ugo = new Wall();
	    }
	} catch (final NullPointerException np) {
	    this.proceed = false;
	    lgo = new Wall();
	    ugo = new Wall();
	}
	return zproceed && (!lgo.hasFriction() || !ugo.hasFriction() || this.mover || this.frozen)
		&& this.canMoveThere();
    }

    private static void freezePlayer() {
	final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	final AbstractCharacter tank = gm.getPlayer();
	final Direction dir = tank.getDirection();
	final int px = gm.getPlayerManager().getPlayerLocationX();
	final int py = gm.getPlayerManager().getPlayerLocationY();
	final int pz = gm.getPlayerManager().getPlayerLocationZ();
	final FrozenParty ft = new FrozenParty(dir, tank.getNumber());
	ft.setSavedObject(tank.getSavedObject());
	GameLogic.morph(ft, px, py, pz, ft.getLayer());
	gm.updatePlayer();
    }

    private void defrostPlayer() {
	if (this.frozen) {
	    this.frozen = false;
	    final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	    final AbstractCharacter tank = gm.getPlayer();
	    final Direction dir = tank.getDirection();
	    final int px = gm.getPlayerManager().getPlayerLocationX();
	    final int py = gm.getPlayerManager().getPlayerLocationY();
	    final int pz = gm.getPlayerManager().getPlayerLocationZ();
	    final Party t = new Party(dir, tank.getNumber());
	    t.setSavedObject(tank.getSavedObject());
	    GameLogic.morph(t, px, py, pz, t.getLayer());
	    gm.updatePlayer();
	    SoundLoader.playSound(SoundConstants.DEFROST);
	}
    }

    private static boolean checkSolid(final AbstractDungeonObject next) {
	final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	// Check cheats
	if (gm.getCheatStatus(GameLogic.CHEAT_GHOSTLY)) {
	    return true;
	} else {
	    return !next.isConditionallySolid();
	}
    }

    static boolean checkSolid(final int zx, final int zy) {
	final GameLogic gm = DungeonDiver7.getApplication().getGameLogic();
	final AbstractDungeonObject next = DungeonDiver7.getApplication().getDungeonManager().getDungeon().getCell(zx,
		zy, gm.getPlayerManager().getPlayerLocationZ(), DungeonConstants.LAYER_LOWER_OBJECTS);
	// Check cheats
	if (gm.getCheatStatus(GameLogic.CHEAT_GHOSTLY)) {
	    return true;
	} else {
	    return !next.isConditionallySolid();
	}
    }

    private boolean areObjectTrackersTracking() {
	boolean result = false;
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isTracking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean areObjectTrackersChecking() {
	boolean result = false;
	for (final MovingObjectTracker tracker : this.objectTrackers) {
	    if (tracker.isChecking()) {
		result = true;
	    }
	}
	return result;
    }

    private boolean areLaserTrackersChecking() {
	boolean result = false;
	for (final MovingLaserTracker tracker : this.laserTrackers) {
	    if (tracker.isChecking()) {
		result = true;
	    }
	}
	return result;
    }

    private void cullTrackers() {
	final MovingObjectTracker[] tempArray1 = this.objectTrackers
		.toArray(new MovingObjectTracker[this.objectTrackers.size()]);
	this.objectTrackers.clear();
	for (final MovingObjectTracker tracker : tempArray1) {
	    if (tracker != null) {
		if (tracker.isTracking()) {
		    this.objectTrackers.add(tracker);
		}
	    }
	}
	final MovingLaserTracker[] tempArray2 = this.laserTrackers
		.toArray(new MovingLaserTracker[this.laserTrackers.size()]);
	this.laserTrackers.clear();
	for (final MovingLaserTracker tracker : tempArray2) {
	    if (tracker != null) {
		if (tracker.isTracking()) {
		    this.laserTrackers.add(tracker);
		}
	    }
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
}
