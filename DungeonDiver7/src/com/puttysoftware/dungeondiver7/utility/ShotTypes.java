/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public class ShotTypes {
    public static final int GREEN = 3;
    public static final int RED = 5;
    public static final int MISSILE = 9;
    public static final int STUNNER = 17;
    public static final int BLUE = 33;
    public static final int DISRUPTOR = 65;
    public static final int POWER = 129;

    private ShotTypes() {
        // Do nothing
    }

    public static final int getRangeTypeForLaserType(final int lt) {
        switch (lt) {
            case STUNNER:
                return RangeTypes.ICE_BOMB;
            case MISSILE:
                return RangeTypes.HEAT_BOMB;
            default:
                return RangeTypes.BOMB;
        }
    }
}
