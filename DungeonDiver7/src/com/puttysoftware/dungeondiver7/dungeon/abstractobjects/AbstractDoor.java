/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public abstract class AbstractDoor extends AbstractDungeonObject {
    // Fields
    private AbstractKey key;

    // Constructors
    protected AbstractDoor(final AbstractKey mgk) {
	super(true);
	this.key = mgk;
	this.type.set(TypeConstants.TYPE_DOOR);
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null) {
	    return false;
	}
	if (this.getClass() != obj.getClass()) {
	    return false;
	}
	final AbstractDoor other = (AbstractDoor) obj;
	if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	final int hash = 7;
	return 71 * hash + (this.key != null ? this.key.hashCode() : 0);
    }

    @Override
    public AbstractDoor clone() {
	final AbstractDoor copy = (AbstractDoor) super.clone();
	copy.key = (AbstractKey) this.key.clone();
	return copy;
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
}