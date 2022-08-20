/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public abstract class AbstractTriggerButtonDoor extends AbstractButtonDoor {
    // Constructors
    protected AbstractTriggerButtonDoor() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_TRIGGER_BUTTON_DOOR);
    }
}