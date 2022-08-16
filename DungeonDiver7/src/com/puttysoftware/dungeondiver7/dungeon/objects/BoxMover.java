/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMover;
import com.puttysoftware.dungeondiver7.locale.Direction;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class BoxMover extends AbstractMover {
    // Constructors
    public BoxMover() {
	super(true);
	this.setDirection(Direction.NORTH);
	this.setFrameNumber(1);
	this.type.set(TypeConstants.TYPE_BOX_MOVER);
    }

    @Override
    public final int getBaseID() {
	return 122;
    }
}