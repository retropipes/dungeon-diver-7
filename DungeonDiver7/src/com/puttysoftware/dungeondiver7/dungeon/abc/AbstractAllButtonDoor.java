/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public abstract class AbstractAllButtonDoor extends AbstractButtonDoor {
    // Constructors
    protected AbstractAllButtonDoor() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_ALL_BUTTON_DOOR);
    }
}