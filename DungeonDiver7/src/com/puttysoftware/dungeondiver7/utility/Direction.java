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

    public int getInternalValue() {
	return this.internalValue;
    }

    public static Direction fromInternalValue(final int iv) {
	switch (iv) {
	case -2:
	    return Direction.INVALID;
	case -1:
	    return Direction.NONE;
	case 0:
	    return Direction.NORTHWEST;
	case 1:
	    return Direction.NORTH;
	case 2:
	    return Direction.NORTHEAST;
	case 3:
	    return Direction.EAST;
	case 4:
	    return Direction.SOUTHEAST;
	case 5:
	    return Direction.SOUTH;
	case 6:
	    return Direction.SOUTHWEST;
	case 7:
	    return Direction.WEST;
	case 8:
	    return Direction.HORIZONTAL;
	case 9:
	    return Direction.VERTICAL;
	default:
	    return Direction.INVALID;
	}
    }
}
