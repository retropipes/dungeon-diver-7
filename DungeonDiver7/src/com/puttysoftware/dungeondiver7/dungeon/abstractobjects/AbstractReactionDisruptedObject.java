/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.utilities.ActionConstants;

public abstract class AbstractReactionDisruptedObject extends AbstractReactionPassThroughObject {
    // Constructors
    protected AbstractReactionDisruptedObject() {
	super();
    }

    @Override
    public boolean acceptTick(final int actionType) {
	return actionType == ActionConstants.ACTION_MOVE;
    }
}