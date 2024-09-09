/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.utility;

public class DirectionRotator {
    public static final int rotate45LeftX(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0 || dirY >= 0) {
		return 1;
	    }
	    return 0;
	}
	if (dirX < 0) {
	    if (dirY > 0) {
		return 0;
	    }
	    return -1;
	}
	if (dirY > 0) {
	    return -1;
	}
	if (dirY < 0) {
	    return 1;
	}
	return 0;
    }

    public static final int rotate45LeftY(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0) {
		return 0;
	    }
	    return -1;
	}
	if (dirX < 0) {
	    if (dirY > 0 || dirY >= 0) {
		return 1;
	    }
	    return 0;
	}
	if (dirY > 0) {
	    return 1;
	}
	if (dirY < 0) {
	    return -1;
	}
	return 0;
    }

    public static final int rotate45RightX(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0) {
		return 0;
	    }
	    return 1;
	}
	if (dirX < 0) {
	    if (dirY > 0 || dirY >= 0) {
		return -1;
	    }
	    return 0;
	}
	if (dirY > 0) {
	    return 1;
	}
	if (dirY < 0) {
	    return -1;
	}
	return 0;
    }

    public static final int rotate45RightY(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0 || dirY >= 0) {
		return 1;
	    }
	    return 0;
	}
	if (dirX < 0) {
	    if (dirY > 0) {
		return 0;
	    }
	    return -1;
	}
	if (dirY > 0) {
	    return 1;
	}
	if (dirY < 0) {
	    return -1;
	}
	return 0;
    }

    private DirectionRotator() {
	// Do nothing
    }
}
