/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class Empty extends AbstractPassThroughObject {
    // Constructors
    public Empty() {
        this.type.set(DungeonObjectTypes.TYPE_EMPTY_SPACE);
    }

    @Override
    public final int getBaseID() {
        return 130;
    }
}