/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.SoundLoader;
import org.retropipes.dungeondiver7.asset.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

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
		this.type.set(DungeonObjectTypes.TYPE_JUMP_OBJECT);
	}

	@Override
	public AbstractJumpObject clone() {
		final var copy = (AbstractJumpObject) super.clone();
		copy.jumpRows = this.jumpRows;
		copy.jumpCols = this.jumpCols;
		return copy;
	}

	@Override
	public AbstractDungeonObject editorPropertiesHook() {
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
		default -> AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
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
		if (!success || this.jumpRows == 0 && this.jumpCols == 0) {
			SoundLoader.playSound(Sounds.LASER_DIE);
		} else {
			SoundLoader.playSound(Sounds.JUMPING);
		}
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		final var app = DungeonDiver7.getStuffBag();
		final var px = app.getGameLogic().getPlayerManager().getPlayerLocationX();
		final var py = app.getGameLogic().getPlayerManager().getPlayerLocationY();
		if (forceUnits > this.getMinimumReactionForce() && this.jumpRows == 0 && this.jumpCols == 0) {
			this.pushCrushAction(locX, locY, locZ);
			return Direction.NONE;
		}
		if (!this.jumpShot) {
			this.jumpShot = true;
			this.dir1X = (int) Math.signum(px - locX);
			this.dir1Y = (int) Math.signum(py - locY);
			SoundLoader.playSound(Sounds.PREPARE);
			return Direction.NONE;
		}
		this.jumpShot = false;
		this.dir2X = (int) Math.signum(px - locX);
		this.dir2Y = (int) Math.signum(py - locY);
		if (this.dir1X != 0 && this.dir2X != 0 || this.dir1Y != 0 && this.dir2Y != 0) {
			SoundLoader.playSound(Sounds.LASER_DIE);
			return Direction.NONE;
		}
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
