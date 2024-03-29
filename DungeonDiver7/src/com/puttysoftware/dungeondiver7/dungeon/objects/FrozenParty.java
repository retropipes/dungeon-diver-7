/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;

public class FrozenParty extends AbstractCharacter {
    // Constructors
    public FrozenParty(final int number) {
        super(number);
    }

    public FrozenParty(final Direction dir, final int number) {
        super(number);
        this.setDirection(dir);
    }

    @Override
    public final int getBaseID() {
        return 15;
    }
}