/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMover;
import com.puttysoftware.dungeondiver7.locale.Direction;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class PartyMover extends AbstractMover {
    // Constructors
    public PartyMover() {
	super();
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
	this.type.set(TypeConstants.TYPE_MOVER);
    }

    @Override
    public final int getBaseID() {
	return 37;
    }
}