/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import java.awt.Color;
import java.io.IOException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class AbstractCharacter extends AbstractDungeonObject {
    // Fields
    private final int characterNumber;

    // Constructors
    protected AbstractCharacter(final int number) {
	super(true);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
	this.type.set(TypeConstants.TYPE_CHARACTER);
	this.characterNumber = number;
    }

    // Methods
    public int getNumber() {
	return this.characterNumber;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_UPPER_OBJECTS;
    }

    @Override
    public int getCustomFormat() {
	return AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
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
    public void timerExpiredAction(final int x, final int y) {
	if (this.getSavedObject() instanceof AbstractMovableObject) {
	    this.getSavedObject().timerExpiredAction(x, y);
	}
	this.activateTimer(1);
    }

    @Override
    public String getCustomText() {
	return Integer.toString(this.characterNumber);
    }

    @Override
    public Color getCustomTextColor() {
	return Color.white;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + this.characterNumber;
	return result;
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
	if (!super.equals(obj)) {
	    return false;
	}
	if (!(obj instanceof AbstractCharacter)) {
	    return false;
	}
	final AbstractCharacter other = (AbstractCharacter) obj;
	if (this.characterNumber != other.characterNumber) {
	    return false;
	}
	return true;
    }

    @Override
    protected void writeDungeonObjectHook(final XDataWriter writer) throws IOException {
	this.getSavedObject().writeDungeonObject(writer);
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
}