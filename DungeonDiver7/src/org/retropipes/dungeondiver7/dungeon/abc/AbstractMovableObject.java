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

public abstract class AbstractMovableObject extends GameObject {
    // Constructors
    protected AbstractMovableObject(final boolean pushable) {
	super(true, pushable, true);
	this.setSavedObject(new Empty());
    }

    @Override
    public boolean canMove() {
	return this.isPushable();
    }

    @Override
    public GameObject clone() {
	final var copy = (AbstractMovableObject) super.clone();
	if (this.getSavedObject() != null) {
	    copy.setSavedObject(this.getSavedObject().clone());
	}
	return copy;
    }

    @Override
    public int getCustomFormat() {
	return GameObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return GameObject.DEFAULT_CUSTOM_VALUE;
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
    protected GameObject readHookV2(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV2(reader, formatVersion));
	return this;
    }

    @Override
    protected GameObject readHookV3(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV3(reader, formatVersion));
	return this;
    }

    @Override
    protected GameObject readHookV4(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV4(reader, formatVersion));
	return this;
    }

    @Override
    protected GameObject readHookV5(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV5(reader, formatVersion));
	return this;
    }

    @Override
    protected GameObject readHookV6(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV6(reader, formatVersion));
	return this;
    }

    @Override
    protected GameObject readHookV7(final DataIOReader reader, final int formatVersion) throws IOException {
	this.setSavedObject(DungeonDiver7.getStuffBag().getObjects().readV7(reader, formatVersion));
	return this;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    protected void writeHook(final DataIOWriter writer) throws IOException {
	this.getSavedObject().write(writer);
    }
}