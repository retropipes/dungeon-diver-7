/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.storage.CloneableObject;
import com.puttysoftware.storage.CloneableObjectStorage;

public class DungeonDataStorage extends CloneableObjectStorage {
	// Constructor
	public DungeonDataStorage(final int... shape) {
		super(shape);
	}

	// Methods
	@Override
	public Object clone() throws CloneNotSupportedException {
		final DungeonDataStorage copy = new DungeonDataStorage(this.getShape());
		for (int x = 0; x < copy.getRawLength(); x++) {
			if (this.getRawCell(x) != null) {
				copy.setRawCell((CloneableObject) this.getRawCell(x).clone(), x);
			}
		}
		return copy;
	}

	public AbstractDungeonObject getDungeonDataCell(final int... loc) {
		return (AbstractDungeonObject) this.getCell(loc);
	}

	public void setDungeonDataCell(final AbstractDungeonObject obj, final int... loc) {
		this.setCell(obj, loc);
	}
}
