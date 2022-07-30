/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utilities;

public class ArrowTypeConstants {
    public static final int LASER_TYPE_GREEN = 3;
    public static final int LASER_TYPE_RED = 5;
    public static final int LASER_TYPE_MISSILE = 9;
    public static final int LASER_TYPE_STUNNER = 17;
    public static final int LASER_TYPE_BLUE = 33;
    public static final int LASER_TYPE_DISRUPTOR = 65;
    public static final int LASER_TYPE_POWER = 129;

    private ArrowTypeConstants() {
	// Do nothing
    }

    public static final int getRangeTypeForLaserType(final int lt) {
	switch (lt) {
	case LASER_TYPE_STUNNER:
	    return RangeTypeConstants.RANGE_TYPE_ICE_BOMB;
	case LASER_TYPE_MISSILE:
	    return RangeTypeConstants.RANGE_TYPE_HEAT_BOMB;
	default:
	    return RangeTypeConstants.RANGE_TYPE_BOMB;
	}
    }
}
