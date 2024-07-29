/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;

public abstract class AbstractTriggerButtonDoor extends AbstractButtonDoor {
    // Constructors
    protected AbstractTriggerButtonDoor() {
	this.type.set(DungeonObjectTypes.TYPE_TRIGGER_BUTTON_DOOR);
    }
}