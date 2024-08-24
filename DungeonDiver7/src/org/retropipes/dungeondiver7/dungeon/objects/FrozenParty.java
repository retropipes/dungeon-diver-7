/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;

public class FrozenParty extends AbstractCharacter {
    public FrozenParty(final Direction dir, final int number) {
	super(number);
	this.setDirection(dir);
    }

    // Constructors
    public FrozenParty(final int number) {
	super(number);
    }

    @Override
    public final int getIdValue() {
	return 15;
    }
}