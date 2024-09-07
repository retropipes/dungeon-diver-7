/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.base;

import org.retropipes.diane.storage.ObjectStorage;
import org.retropipes.dungeondiver7.dungeon.gameobject.GameObject;

public class DungeonDataStorage extends ObjectStorage<GameObject> {
    public DungeonDataStorage(final DungeonDataStorage source) {
	super(source);
    }

    // Constructor
    public DungeonDataStorage(final int... shape) {
	super(shape);
    }

    public GameObject getDungeonDataCell(final int... loc) {
	return (GameObject) this.getCell(loc);
    }

    public void setDungeonDataCell(final GameObject obj, final int... loc) {
	this.setCell(obj, loc);
    }
}