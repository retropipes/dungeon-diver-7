/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public enum Direction {
    INVALID(-2),
    NONE(-1),
    NORTHWEST(0),
    NORTH(1),
    NORTHEAST(2),
    EAST(3),
    SOUTHEAST(4),
    SOUTH(5),
    SOUTHWEST(6),
    WEST(7),
    HORIZONTAL(8),
    VERTICAL(9);

    int internalValue;

    Direction(final int v) {
	this.internalValue = v;
    }

    int getInternalValue() {
	return this.internalValue;
    }
}
