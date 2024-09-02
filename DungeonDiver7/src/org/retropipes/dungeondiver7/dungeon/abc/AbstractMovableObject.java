/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractMovableObject extends DungeonObject {
    // Fields
    private boolean waitingOnTunnel;

    // Constructors
    protected AbstractMovableObject(final boolean pushable) {
	super(true, pushable, true);
	this.setSavedObject(new Empty());
	this.waitingOnTunnel = false;
    }

    @Override
    public boolean canMove() {
	return this.isPushable();
    }

    @Override
    public DungeonObject clone() {
	final var copy = (AbstractMovableObject) super.clone();
	if (this.getSavedObject() != null) {
	    copy.setSavedObject(this.getSavedObject().clone());
	}
	return copy;
    }

    @Override
    public int getCustomFormat() {
	return DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return DungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    public abstract void playSoundHook();

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    protected DungeonObject readHookV2(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV2(reader, formatVersion));
	return this;
    }

    @Override
    protected DungeonObject readHookV3(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV3(reader, formatVersion));
	return this;
    }

    @Override
    protected DungeonObject readHookV4(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV4(reader, formatVersion));
	return this;
    }

    @Override
    protected DungeonObject readHookV5(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV5(reader, formatVersion));
	return this;
    }

    @Override
    protected DungeonObject readHookV6(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV6(reader, formatVersion));
	return this;
    }

    @Override
    protected DungeonObject readHookV7(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV7(reader, formatVersion));
	return this;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    public final void setWaitingOnTunnel(final boolean value) {
	this.waitingOnTunnel = value;
    }

    public final boolean waitingOnTunnel() {
	return this.waitingOnTunnel;
    }

    @Override
    protected void writeHook(final DataIOWriter writer) throws IOException {
	this.getSavedObject().write(writer);
    }
}