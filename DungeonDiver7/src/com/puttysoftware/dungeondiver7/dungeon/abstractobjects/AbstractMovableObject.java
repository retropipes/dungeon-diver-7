/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class AbstractMovableObject extends AbstractDungeonObject {
    // Fields
    private boolean waitingOnTunnel;

    // Constructors
    protected AbstractMovableObject(final boolean pushable) {
	super(true, pushable, true);
	this.setSavedObject(new Empty());
	this.waitingOnTunnel = false;
	this.type.set(TypeConstants.TYPE_MOVABLE);
    }

    public final boolean waitingOnTunnel() {
	return this.waitingOnTunnel;
    }

    public final void setWaitingOnTunnel(final boolean value) {
	this.waitingOnTunnel = value;
    }

    @Override
    public AbstractDungeonObject clone() {
	final AbstractMovableObject copy = (AbstractMovableObject) super.clone();
	if (this.getSavedObject() != null) {
	    copy.setSavedObject(this.getSavedObject().clone());
	}
	return copy;
    }

    @Override
    public boolean canMove() {
	return this.isPushable();
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    public abstract void playSoundHook();

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Application app = DungeonDiver7.getApplication();
	if (this.canMove()) {
	    if (forceUnits >= this.getMinimumReactionForce()) {
		try {
		    final AbstractDungeonObject mof = app.getDungeonManager().getDungeon().getCell(locX + dirX,
			    locY + dirY, locZ, this.getLayer());
		    final AbstractDungeonObject mor = app.getDungeonManager().getDungeon().getCell(locX - dirX,
			    locY - dirY, locZ, this.getLayer());
		    if (this.getMaterial() == MaterialConstants.MATERIAL_MAGNETIC) {
			if (laserType == ArrowTypeConstants.LASER_TYPE_BLUE && mof != null
				&& (mof.isOfType(TypeConstants.TYPE_CHARACTER) || !mof.isSolid())) {
			    app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
			    this.playSoundHook();
			} else if (mor != null && (mor.isOfType(TypeConstants.TYPE_CHARACTER) || !mor.isSolid())) {
			    app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
			    this.playSoundHook();
			} else {
			    // Object doesn't react to this type of laser
			    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
			}
		    } else {
			if (laserType == ArrowTypeConstants.LASER_TYPE_BLUE && mor != null
				&& (mor.isOfType(TypeConstants.TYPE_CHARACTER) || !mor.isSolid())) {
			    app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
			    this.playSoundHook();
			} else if (mof != null && (mof.isOfType(TypeConstants.TYPE_CHARACTER) || !mof.isSolid())) {
			    app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
			    this.playSoundHook();
			} else {
			    // Object doesn't react to this type of laser
			    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
			}
		    }
		} catch (final ArrayIndexOutOfBoundsException aioobe) {
		    // Object can't go that way
		    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
	    } else {
		// Not enough force
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	} else {
	    // Object is not movable
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return Direction.NONE;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    protected AbstractDungeonObject readDungeonObjectHookG2(final XDataReader reader, final int formatVersion)
	    throws IOException {
	this.setSavedObject(DungeonDiver7.getApplication().getObjects().readDungeonObjectG2(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractDungeonObject readDungeonObjectHookG3(final XDataReader reader, final int formatVersion)
	    throws IOException {
	this.setSavedObject(DungeonDiver7.getApplication().getObjects().readDungeonObjectG3(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractDungeonObject readDungeonObjectHookG4(final XDataReader reader, final int formatVersion)
	    throws IOException {
	this.setSavedObject(DungeonDiver7.getApplication().getObjects().readDungeonObjectG4(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractDungeonObject readDungeonObjectHookG5(final XDataReader reader, final int formatVersion)
	    throws IOException {
	this.setSavedObject(DungeonDiver7.getApplication().getObjects().readDungeonObjectG5(reader, formatVersion));
	return this;
    }

    @Override
    protected AbstractDungeonObject readDungeonObjectHookG6(final XDataReader reader, final int formatVersion)
	    throws IOException {
	this.setSavedObject(DungeonDiver7.getApplication().getObjects().readDungeonObjectG6(reader, formatVersion));
	return this;
    }

    @Override
    protected void writeDungeonObjectHook(final XDataWriter writer) throws IOException {
	this.getSavedObject().writeDungeonObject(writer);
    }

    @Override
    public int getCustomFormat() {
	return AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
    }

    @Override
    public boolean doLasersPassThrough() {
	return false;
    }
}