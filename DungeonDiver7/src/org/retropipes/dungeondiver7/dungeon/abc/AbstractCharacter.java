/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import java.awt.Color;
import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractCharacter extends DungeonObject {
    // Fields
    private final int characterNumber;

    // Constructors
    protected AbstractCharacter() {
	super(true);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
	this.characterNumber = 1;
    }

    protected AbstractCharacter(final int number) {
	super(true);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
	this.characterNumber = number;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj) || !(obj instanceof final AbstractCharacter other)
		|| this.characterNumber != other.characterNumber) {
	    return false;
	}
	return true;
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
    public String getCustomText() {
	return Integer.toString(this.characterNumber);
    }

    @Override
    public Color getCustomTextColor() {
	return Color.white;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_UPPER_OBJECTS;
    }

    public int getNumber() {
	return this.characterNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final var prime = 31;
	final var result = super.hashCode();
	return prime * result + this.characterNumber;
    }

    @Override
    public final boolean isPlayer() {
	return true;
    }

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

    @Override
    public void timerExpiredAction(final int x, final int y) {
	if (this.getSavedObject() instanceof AbstractMovableObject) {
	    this.getSavedObject().timerExpiredAction(x, y);
	}
	this.activateTimer(1);
    }

    @Override
    protected void writeHook(final DataIOWriter writer) throws IOException {
	this.getSavedObject().write(writer);
    }
}