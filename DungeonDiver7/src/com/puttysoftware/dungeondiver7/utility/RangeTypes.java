/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public class RangeTypes {
    public static final int BOMB = 0;
    public static final int HEAT_BOMB = 1;
    public static final int ICE_BOMB = 2;

    private RangeTypes() {
	// Do nothing
    }

    public static final int getMaterialForRangeType(final int rt) {
	switch (rt) {
	case BOMB:
	    return Materials.METALLIC;
	case HEAT_BOMB:
	    return Materials.FIRE;
	case ICE_BOMB:
	    return Materials.ICE;
	default:
	    return Materials.DEFAULT;
	}
    }
}
