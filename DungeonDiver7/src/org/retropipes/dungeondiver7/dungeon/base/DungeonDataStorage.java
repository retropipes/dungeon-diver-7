/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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
}
