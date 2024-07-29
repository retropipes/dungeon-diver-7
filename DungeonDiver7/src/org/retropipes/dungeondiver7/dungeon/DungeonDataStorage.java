/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon;

import org.retropipes.diane.storage.ObjectStorage;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;

public class DungeonDataStorage extends ObjectStorage<AbstractDungeonObject> {
    public DungeonDataStorage(final DungeonDataStorage source) {
	super(source);
    }

    // Constructor
    public DungeonDataStorage(final int... shape) {
	super(shape);
    }

    public AbstractDungeonObject getDungeonDataCell(final int... loc) {
	return (AbstractDungeonObject) this.getCell(loc);
    }

    public void setDungeonDataCell(final AbstractDungeonObject obj, final int... loc) {
	this.setCell(obj, loc);
    }
}
