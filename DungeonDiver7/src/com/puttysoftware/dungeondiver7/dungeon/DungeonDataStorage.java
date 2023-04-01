/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.diane.storage.ObjectStorage;

public class DungeonDataStorage extends ObjectStorage {
    // Constructor
    public DungeonDataStorage(final int... shape) {
        super(shape);
    }

    public DungeonDataStorage(final DungeonDataStorage source) {
        super(source);
    }

    public AbstractDungeonObject getDungeonDataCell(final int... loc) {
        return (AbstractDungeonObject) this.getCell(loc);
    }

    public void setDungeonDataCell(final AbstractDungeonObject obj, final int... loc) {
        this.setCell(obj, loc);
    }
}
