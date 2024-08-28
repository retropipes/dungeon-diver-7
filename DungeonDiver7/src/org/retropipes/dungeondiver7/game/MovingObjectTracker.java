/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game;

import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractJumpObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

final class MovingObjectTracker {
    private static boolean checkSolid(final DungeonObject next) {
	final var nextSolid = next.isConditionallySolid();
	if (!nextSolid || next.isPlayer()) {
	    return true;
	}
	return false;
    }

    // Fields
    private boolean objectMoving;
    private int objCumX, objCumY, objIncX, objIncY;
    private int objMultX, objMultY;
    private DungeonObject belowUpper;
    private DungeonObject belowLower;
    private AbstractMovableObject movingObj;
    private boolean objectCheck;
    private boolean objectNewlyActivated;
    private boolean jumpOnMover;

    // Constructors
    public MovingObjectTracker() {
	this.resetTracker();
    }

    void activateObject(final int zx, final int zy, final int pushX, final int pushY, final AbstractMovableObject gmo) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	final var plMgr = gm.getPlayerManager();
	final var pz = plMgr.getPlayerLocationZ();
	this.objIncX = pushX - zx;
	this.objIncY = pushY - zy;
	if (gmo instanceof AbstractJumpObject) {
	    if (pushX - zx == 0) {
		if (pushY > zy) {
		    this.objIncX = -1;
		} else {
		    this.objIncX = 1;
		}
	    }
	    if (pushY - zy == 0) {
		if (pushX > zx) {
		    this.objIncY = 1;
		} else {
		    this.objIncY = -1;
		}
	    }
	}
	this.objCumX = zx;
	this.objCumY = zy;
	this.movingObj = gmo;
	this.belowUpper = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		DungeonConstants.LAYER_UPPER_GROUND);
	this.belowLower = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		DungeonConstants.LAYER_LOWER_GROUND);
	this.objectMoving = true;
	this.objectCheck = true;
	this.objectNewlyActivated = true;
    }

    private void doJumpObjectOnce(final AbstractJumpObject jumper) {
	final var app = DungeonDiver7.getStuffBag();
	final var m = app.getDungeonManager().getDungeon();
	final var gm = app.getGameLogic();
	final var pz = gm.getPlayerManager().getPlayerLocationZ();
	try {
	    this.jumpOnMover = false;
	    this.objMultX = jumper.getActualJumpCols();
	    this.objMultY = jumper.getActualJumpRows();
	    if (gm.isDelayedDecayActive() && gm.isRemoteDecayActive()) {
		gm.doRemoteDelayedDecay(jumper);
	    }
	    final var oldSave = jumper.getSavedObject();
	    final var saved = m.getCell(this.objCumX + this.objIncX * this.objMultX,
		    this.objCumY + this.objIncY * this.objMultY, pz, jumper.getLayer());
	    this.belowUpper = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    DungeonConstants.LAYER_UPPER_GROUND);
	    this.belowLower = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    DungeonConstants.LAYER_LOWER_GROUND);
	    if (MovingObjectTracker.checkSolid(saved) && this.objMultX != 0 && this.objMultY != 0) {
		jumper.jumpSound(true);
		oldSave.pushOutAction(jumper, this.objCumX, this.objCumY, pz);
		m.setCell(oldSave, this.objCumX, this.objCumY, pz, jumper.getLayer());
		jumper.setSavedObject(saved);
		m.setCell(jumper, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz, jumper.getLayer());
		var stopObj = this.belowLower.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		final var temp1 = this.belowUpper.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp1) {
		    stopObj = false;
		}
		final var temp2 = saved.pushIntoAction(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp2) {
		    stopObj = false;
		}
		this.objectMoving = stopObj;
		this.objectCheck = stopObj;
		final var oldObjIncX = this.objIncX;
		final var oldObjIncY = this.objIncY;
		if (this.belowUpper == null || this.belowLower == null) {
		    this.objectCheck = false;
		} else if (!jumper.hasFriction()) {
		    // Handle icy objects
		    this.objectCheck = true;
		} else if (this.belowUpper.isOfType(DungeonObjectTypes.TYPE_ANTI_MOVER) && jumper.canShoot()) {
		    // Handle anti-tank on anti-tank mover
		    final var dir = this.belowUpper.getDirection();
		    final var unres = DirectionResolver.unresolve(dir);
		    this.objIncX = unres[0];
		    this.objIncY = unres[1];
		    this.objectCheck = true;
		} else if (this.belowUpper.isOfType(DungeonObjectTypes.TYPE_BOX_MOVER) && jumper.canMove()) {
		    // Handle box on box mover
		    this.jumpOnMover = true;
		    final var dir = this.belowUpper.getDirection();
		    final var unres = DirectionResolver.unresolve(dir);
		    this.objIncX = unres[0];
		    this.objIncY = unres[1];
		    this.objectCheck = true;
		} else if (this.belowUpper.isOfType(DungeonObjectTypes.TYPE_MIRROR_MOVER)
			&& this.movingObj.isOfType(DungeonObjectTypes.TYPE_MOVABLE_MIRROR)) {
		    // Handle mirror on mirror mover
		    final var dir = this.belowUpper.getDirection();
		    final var unres = DirectionResolver.unresolve(dir);
		    this.objIncX = unres[0];
		    this.objIncY = unres[1];
		    this.objectCheck = true;
		} else {
		    this.objectCheck = !this.belowLower.hasFriction() || !this.belowUpper.hasFriction();
		}
		if (this.objIncX != oldObjIncX || this.objIncY != oldObjIncY) {
		    if (this.jumpOnMover) {
			this.objCumX += oldObjIncX;
			this.objCumY += oldObjIncY;
		    } else {
			this.objCumX += oldObjIncX * this.objMultX;
			this.objCumY += oldObjIncY * this.objMultY;
		    }
		} else if (this.jumpOnMover) {
		    this.objCumX += this.objIncX;
		    this.objCumY += this.objIncY;
		} else {
		    this.objCumX += this.objIncX * this.objMultX;
		    this.objCumY += this.objIncY * this.objMultY;
		}
		app.getDungeonManager().setDirty(true);
	    } else {
		// Movement failed
		jumper.jumpSound(false);
		oldSave.pushIntoAction(jumper, this.objCumX, this.objCumY, pz);
		jumper.pushCollideAction(this.movingObj, this.objCumX, this.objCumY, pz);
		saved.pushCollideAction(jumper, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		this.objectMoving = false;
		this.objectCheck = false;
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    jumper.jumpSound(false);
	    this.objectMoving = false;
	    this.objectCheck = false;
	}
	gm.redrawDungeon();
    }

    private void doNormalObjectOnce() {
	final var app = DungeonDiver7.getStuffBag();
	final var m = app.getDungeonManager().getDungeon();
	final var gm = app.getGameLogic();
	final var pz = gm.getPlayerManager().getPlayerLocationZ();
	try {
	    if (gm.isDelayedDecayActive() && gm.isRemoteDecayActive()) {
		gm.doRemoteDelayedDecay(this.movingObj);
	    }
	    final var oldSave = this.movingObj.getSavedObject();
	    final var saved = m.getCell(this.objCumX + this.objIncX * this.objMultX,
		    this.objCumY + this.objIncY * this.objMultY, pz, this.movingObj.getLayer());
	    this.belowUpper = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    DungeonConstants.LAYER_UPPER_GROUND);
	    this.belowLower = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		    this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz,
		    DungeonConstants.LAYER_LOWER_GROUND);
	    if (MovingObjectTracker.checkSolid(saved)) {
		this.belowLower.pushOutAction(this.movingObj, this.objCumX, this.objCumY, pz);
		this.belowUpper.pushOutAction(this.movingObj, this.objCumX, this.objCumY, pz);
		oldSave.pushOutAction(this.movingObj, this.objCumX, this.objCumY, pz);
		m.setCell(oldSave, this.objCumX, this.objCumY, pz, this.movingObj.getLayer());
		this.movingObj.setSavedObject(saved);
		m.setCell(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz, this.movingObj.getLayer());
		var stopObj = this.belowLower.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		final var temp1 = this.belowUpper.pushIntoAction(this.movingObj,
			this.objCumX + this.objIncX * this.objMultX, this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp1) {
		    stopObj = false;
		}
		final var temp2 = saved.pushIntoAction(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		if (!temp2) {
		    stopObj = false;
		}
		this.objectMoving = stopObj;
		this.objectCheck = stopObj;
		final var oldObjIncX = this.objIncX;
		final var oldObjIncY = this.objIncY;
		if (this.belowUpper == null || this.belowLower == null) {
		    this.objectCheck = false;
		} else if (!this.movingObj.hasFriction()) {
		    // Handle icy objects
		    this.objectCheck = true;
		} else if (this.belowUpper.isOfType(DungeonObjectTypes.TYPE_ANTI_MOVER) && this.movingObj.canShoot()
			|| this.belowUpper.isOfType(DungeonObjectTypes.TYPE_BOX_MOVER) && this.movingObj.canMove()) {
		    // Handle anti-tank on anti-tank mover
		    final var dir = this.belowUpper.getDirection();
		    final var unres = DirectionResolver.unresolve(dir);
		    this.objIncX = unres[0];
		    this.objIncY = unres[1];
		    this.objectCheck = true;
		} else if (this.belowUpper.isOfType(DungeonObjectTypes.TYPE_MIRROR_MOVER)
			&& this.movingObj.isOfType(DungeonObjectTypes.TYPE_MOVABLE_MIRROR)) {
		    // Handle mirror on mirror mover
		    final var dir = this.belowUpper.getDirection();
		    final var unres = DirectionResolver.unresolve(dir);
		    this.objIncX = unres[0];
		    this.objIncY = unres[1];
		    this.objectCheck = true;
		} else {
		    this.objectCheck = !this.belowLower.hasFriction() || !this.belowUpper.hasFriction();
		}
		if (this.objIncX != oldObjIncX || this.objIncY != oldObjIncY) {
		    this.objCumX += oldObjIncX;
		    this.objCumY += oldObjIncY;
		} else {
		    this.objCumX += this.objIncX;
		    this.objCumY += this.objIncY;
		}
		app.getDungeonManager().setDirty(true);
	    } else {
		// Movement failed
		this.belowLower.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		this.belowUpper.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		oldSave.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		this.movingObj.pushCollideAction(this.movingObj, this.objCumX, this.objCumY, pz);
		saved.pushCollideAction(this.movingObj, this.objCumX + this.objIncX * this.objMultX,
			this.objCumY + this.objIncY * this.objMultY, pz);
		this.objectMoving = false;
		this.objectCheck = false;
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    this.objectMoving = false;
	    this.objectCheck = false;
	}
	gm.redrawDungeon();
    }

    private void doObjectOnce() {
	if (this.movingObj instanceof AbstractJumpObject) {
	    this.doJumpObjectOnce((AbstractJumpObject) this.movingObj);
	} else {
	    this.doNormalObjectOnce();
	}
    }

    void haltMovingObject() {
	this.objectMoving = false;
	this.objectCheck = false;
    }

    boolean isChecking() {
	return this.objectCheck;
    }

    boolean isTracking() {
	return this.objectMoving;
    }

    void resetTracker() {
	this.objectCheck = true;
	this.objectNewlyActivated = false;
	this.jumpOnMover = false;
	this.objMultX = 1;
	this.objMultY = 1;
    }

    void trackPart1() {
	if (this.objectMoving && this.objectCheck) {
	    this.doObjectOnce();
	}
    }

    void trackPart2() {
	try {
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    final var plMgr = gm.getPlayerManager();
	    final var pz = plMgr.getPlayerLocationZ();
	    if (this.objectMoving) {
		// Make objects pushed into ice move 2 squares first time
		if (this.objectCheck && this.objectNewlyActivated
			&& (!this.belowLower.hasFriction() || !this.belowUpper.hasFriction())) {
		    this.doObjectOnce();
		    this.objectCheck = !this.belowLower.hasFriction() || !this.belowUpper.hasFriction();
		}
	    } else {
		this.objectCheck = false;
		// Check for moving object stopped on thin ice
		if (this.movingObj != null && gm.isDelayedDecayActive() && gm.isRemoteDecayActive()) {
		    gm.doRemoteDelayedDecay(this.movingObj);
		    this.belowUpper.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		    this.belowLower.pushIntoAction(this.movingObj, this.objCumX, this.objCumY, pz);
		}
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Stop object
	    this.objectMoving = false;
	    this.objectCheck = false;
	}
    }

    void trackPart3() {
	if (!this.objectCheck) {
	    this.objectMoving = false;
	}
    }

    void trackPart4() {
	this.objectNewlyActivated = false;
    }
}
