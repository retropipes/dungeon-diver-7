/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import java.io.IOException;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;

public abstract class AbstractMovableObject extends AbstractDungeonObject {
    // Fields
    private boolean waitingOnTunnel;

    // Constructors
    protected AbstractMovableObject(final boolean pushable) {
        super(true, pushable, true);
        this.setSavedObject(new Empty());
        this.waitingOnTunnel = false;
        this.type.set(DungeonObjectTypes.TYPE_MOVABLE);
    }

    public final boolean waitingOnTunnel() {
        return this.waitingOnTunnel;
    }

    public final void setWaitingOnTunnel(final boolean value) {
        this.waitingOnTunnel = value;
    }

    @Override
    public AbstractDungeonObject clone() {
        final var copy = (AbstractMovableObject) super.clone();
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
        final var app = DungeonDiver7.getStuffBag();
        if (!this.canMove() || forceUnits < this.getMinimumReactionForce()) {
            // Not enough force
            return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
        }
        try {
            final var mof = app.getDungeonManager().getDungeon().getCell(locX + dirX, locY + dirY, locZ,
                    this.getLayer());
            final var mor = app.getDungeonManager().getDungeon().getCell(locX - dirX, locY - dirY, locZ,
                    this.getLayer());
            if (this.getMaterial() == Materials.MAGNETIC) {
                if (laserType == ShotTypes.BLUE && mof != null
                        && (mof.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mof.isSolid())) {
                    app.getGameLogic().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
                } else if (mor != null && (mor.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mor.isSolid())) {
                    app.getGameLogic().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
                } else {
                    // Object doesn't react to this type of laser
                    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
                }
            } else if (laserType == ShotTypes.BLUE && mor != null
                    && (mor.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mor.isSolid())) {
                app.getGameLogic().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
            } else if (mof != null && (mof.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mof.isSolid())) {
                app.getGameLogic().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
            } else {
                // Object doesn't react to this type of laser
                return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
            }
            this.playSoundHook();
        } catch (final ArrayIndexOutOfBoundsException aioobe) {
            // Object can't go that way
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
    protected AbstractDungeonObject readHookV2(final DataIOReader reader, final int formatVersion) throws IOException {
        this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV2(reader, formatVersion));
        return this;
    }

    @Override
    protected AbstractDungeonObject readHookV3(final DataIOReader reader, final int formatVersion) throws IOException {
        this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV3(reader, formatVersion));
        return this;
    }

    @Override
    protected AbstractDungeonObject readHookV4(final DataIOReader reader, final int formatVersion) throws IOException {
        this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV4(reader, formatVersion));
        return this;
    }

    @Override
    protected AbstractDungeonObject readHookV5(final DataIOReader reader, final int formatVersion) throws IOException {
        this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV5(reader, formatVersion));
        return this;
    }

    @Override
    protected AbstractDungeonObject readHookV6(final DataIOReader reader, final int formatVersion) throws IOException {
        this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV6(reader, formatVersion));
        return this;
    }

    @Override
    protected AbstractDungeonObject readHookV7(final DataIOReader reader, final int formatVersion) throws IOException {
        this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV7(reader, formatVersion));
        return this;
    }

    @Override
    protected void writeHook(final DataIOWriter writer) throws IOException {
        this.getSavedObject().write(writer);
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