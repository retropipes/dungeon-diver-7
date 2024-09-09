/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.utility;

public class ShotTypes {
    public static final int GREEN = 3;
    public static final int RED = 5;
    public static final int MISSILE = 9;
    public static final int STUNNER = 17;
    public static final int BLUE = 33;
    public static final int DISRUPTOR = 65;
    public static final int POWER = 129;

    public static final int getRangeTypeForLaserType(final int lt) {
	return switch (lt) {
	case STUNNER -> RangeTypes.ICE_BOMB;
	case MISSILE -> RangeTypes.HEAT_BOMB;
	default -> RangeTypes.BOMB;
	};
    }

    private ShotTypes() {
	// Do nothing
    }
}
