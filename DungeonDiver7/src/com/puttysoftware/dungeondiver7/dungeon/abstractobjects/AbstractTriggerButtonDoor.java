/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public abstract class AbstractTriggerButtonDoor extends AbstractButtonDoor {
    // Constructors
    protected AbstractTriggerButtonDoor() {
	super();
	this.type.set(TypeConstants.TYPE_TRIGGER_BUTTON_DOOR);
    }
}