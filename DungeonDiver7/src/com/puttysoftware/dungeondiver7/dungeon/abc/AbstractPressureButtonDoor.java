/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractPressureButtonDoor extends AbstractButtonDoor {
    // Constructors
    protected AbstractPressureButtonDoor() {
	super();
	this.type.set(TypeConstants.TYPE_PRESSURE_BUTTON_DOOR);
    }
}