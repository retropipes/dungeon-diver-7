/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;

public abstract class AbstractJumpObject extends AbstractMovableObject {
    // Fields
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
    }

    @Override
    public AbstractJumpObject clone() {
	final var copy = (AbstractJumpObject) super.clone();
	copy.jumpRows = this.jumpRows;
	copy.jumpCols = this.jumpCols;
	return copy;
    }

    @Override
    public GameObject editorPropertiesHook() {
	DungeonDiver7.getStuffBag().getEditor().editJumpBox(this);
	return this;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj) || !(obj instanceof final AbstractJumpObject other) || this.jumpCols != other.jumpCols
		|| this.jumpRows != other.jumpRows) {
	    return false;
	}
	return true;
    }

    public int getActualJumpCols() {
	if (this.flip) {
	    if (this.dir2Y == 0) {
		return this.jumpRows * this.dir1Y;
	    }
	    return this.jumpRows * this.dir2Y;
	}
	if (this.dir2X == 0) {
	    return this.jumpCols * this.dir1X;
	}
	return this.jumpCols * this.dir2X;
    }

    public int getActualJumpRows() {
	if (this.flip) {
	    if (this.dir2X == 0) {
		return this.jumpCols * this.dir1X;
	    }
	    return this.jumpCols * this.dir2X;
	}
	if (this.dir2Y == 0) {
	    return this.jumpRows * this.dir1Y;
	}
	return this.jumpRows * this.dir2Y;
    }

    @Override
    public int getCustomFormat() {
	return 2;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return switch (propID) {
	case 1 -> this.jumpRows;
	case 2 -> this.jumpCols;
	default -> GameObject.DEFAULT_CUSTOM_VALUE;
	};
    }

    @Override
    public String getCustomText() {
	final var sb = new StringBuilder();
	sb.append(this.jumpCols);
	sb.append(",");
	sb.append(this.jumpRows);
	return sb.toString();
    }

    public final int getJumpCols() {
	return this.jumpCols;
    }

    public final int getJumpRows() {
	return this.jumpRows;
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	var result = super.hashCode();
	result = prime * result + this.jumpCols;
	return prime * result + this.jumpRows;
    }

    public final void jumpSound(final boolean success) {
	// Do nothing
    }

    @Override
    public void playSoundHook() {
	// Do nothing
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

    public final void setJumpCols(final int njc) {
	this.jumpCols = njc;
    }

    public final void setJumpRows(final int njr) {
	this.jumpRows = njr;
    }
}
