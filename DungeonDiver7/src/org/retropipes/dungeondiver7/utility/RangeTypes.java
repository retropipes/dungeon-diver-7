/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.utility;

import org.retropipes.dungeondiver7.gameobject.Material;

public class RangeTypes {
    public static final int BOMB = 0;
    public static final int HEAT_BOMB = 1;
    public static final int ICE_BOMB = 2;

    public static final Material getMaterialForRangeType(final int rt) {
	return switch (rt) {
	case BOMB -> Material.METALLIC;
	case HEAT_BOMB -> Material.FIRE;
	case ICE_BOMB -> Material.ICE;
	default -> Material.DEFAULT;
	};
    }

    private RangeTypes() {
	// Do nothing
    }
}
