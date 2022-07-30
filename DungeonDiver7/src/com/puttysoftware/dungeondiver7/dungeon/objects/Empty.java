/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class Empty extends AbstractPassThroughObject {
    // Constructors
    public Empty() {
	super();
	this.type.set(TypeConstants.TYPE_EMPTY_SPACE);
    }

    @Override
    public final int getStringBaseID() {
	return 130;
    }
}