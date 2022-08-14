/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractJumpObject extends AbstractMovableObject {
    // Fields
    private boolean jumpShot;
    private boolean flip;
    private int dir1X;
    private int dir1Y;
    private int dir2X;
    private int dir2Y;
    private int jumpRows;
    private int jumpCols;

    // Constructors
    protected AbstractJumpObject() {
	super(true);
	this.jumpRows = 0;
	this.jumpCols = 0;
	this.jumpShot = false;
	this.type.set(TypeConstants.TYPE_JUMP_OBJECT);
    }

    public int getActualJumpRows() {
	if (this.flip) {
	    if (this.dir2X == 0) {
		return this.jumpCols * this.dir1X;
	    } else {
		return this.jumpCols * this.dir2X;
	    }
	} else {
	    if (this.dir2Y == 0) {
		return this.jumpRows * this.dir1Y;
	    } else {
		return this.jumpRows * this.dir2Y;
	    }
	}
    }

    public int getActualJumpCols() {
	if (this.flip) {
	    if (this.dir2Y == 0) {
		return this.jumpRows * this.dir1Y;
	    } else {
		return this.jumpRows * this.dir2Y;
	    }
	} else {
	    if (this.dir2X == 0) {
		return this.jumpCols * this.dir1X;
	    } else {
		return this.jumpCols * this.dir2X;
	    }
	}
    }

    public final int getJumpRows() {
	return this.jumpRows;
    }

    public final int getJumpCols() {
	return this.jumpCols;
    }

    public final void setJumpRows(final int njr) {
	this.jumpRows = njr;
    }

    public final void setJumpCols(final int njc) {
	this.jumpCols = njc;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final int px = app.getGameLogic().getPlayerManager().getPlayerLocationX();
	final int py = app.getGameLogic().getPlayerManager().getPlayerLocationY();
	if (forceUnits > this.getMinimumReactionForce() && this.jumpRows == 0 && this.jumpCols == 0) {
	    this.pushCrushAction(locX, locY, locZ);
	    return Direction.NONE;
	} else {
	    if (this.jumpShot) {
		this.jumpShot = false;
		this.dir2X = (int) Math.signum(px - locX);
		this.dir2Y = (int) Math.signum(py - locY);
		if (this.dir1X != 0 && this.dir2X != 0 || this.dir1Y != 0 && this.dir2Y != 0) {
		    SoundLoader.playSound(SoundConstants.LASER_DIE);
		    return Direction.NONE;
		} else {
		    if (this.dir1X == 0 && this.dir2X == 1 && this.dir1Y == -1 && this.dir2Y == 0
			    || this.dir1X == 0 && this.dir2X == -1 && this.dir1Y == 1 && this.dir2Y == 0
			    || this.dir1X == 1 && this.dir2X == 0 && this.dir1Y == 0 && this.dir2Y == -1
			    || this.dir1X == -1 && this.dir2X == 0 && this.dir1Y == 0 && this.dir2Y == 1) {
			this.flip = true;
		    } else {
			this.flip = false;
		    }
		    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
	    } else {
		this.jumpShot = true;
		this.dir1X = (int) Math.signum(px - locX);
		this.dir1Y = (int) Math.signum(py - locY);
		SoundLoader.playSound(SoundConstants.PREPARE);
		return Direction.NONE;
	    }
	}
    }

    @Override
    public AbstractDungeonObject editorPropertiesHook() {
	DungeonDiver7.getStuffBag().getEditor().editJumpBox(this);
	return this;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + this.jumpCols;
	return prime * result + this.jumpRows;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj)) {
	    return false;
	}
	if (!(obj instanceof AbstractJumpObject)) {
	    return false;
	}
	final AbstractJumpObject other = (AbstractJumpObject) obj;
	if (this.jumpCols != other.jumpCols) {
	    return false;
	}
	if (this.jumpRows != other.jumpRows) {
	    return false;
	}
	return true;
    }

    @Override
    public AbstractJumpObject clone() {
	final AbstractJumpObject copy = (AbstractJumpObject) super.clone();
	copy.jumpRows = this.jumpRows;
	copy.jumpCols = this.jumpCols;
	return copy;
    }

    @Override
    public void playSoundHook() {
	// Do nothing
    }

    public final void jumpSound(final boolean success) {
	if (success) {
	    if (this.jumpRows == 0 && this.jumpCols == 0) {
		SoundLoader.playSound(SoundConstants.LASER_DIE);
	    } else {
		SoundLoader.playSound(SoundConstants.JUMPING);
	    }
	} else {
	    SoundLoader.playSound(SoundConstants.LASER_DIE);
	}
    }

    @Override
    public String getCustomText() {
	final StringBuilder sb = new StringBuilder();
	sb.append(this.jumpCols);
	sb.append(",");
	sb.append(this.jumpRows);
	return sb.toString();
    }

    @Override
    public int getCustomFormat() {
	return 2;
    }

    @Override
    public int getCustomProperty(final int propID) {
	switch (propID) {
	case 1:
	    return this.jumpRows;
	case 2:
	    return this.jumpCols;
	default:
	    return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
	}
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	switch (propID) {
	case 1:
	    this.jumpRows = value;
	    break;
	case 2:
	    this.jumpCols = value;
	    break;
	default:
	    break;
	}
    }
}
