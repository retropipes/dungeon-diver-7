/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public class DirectionRotator {
    private DirectionRotator() {
	// Do nothing
    }

    public static final int rotate45LeftX(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0) {
		return 1;
	    } else if (dirY < 0) {
		return 0;
	    } else {
		return 1;
	    }
	} else if (dirX < 0) {
	    if (dirY > 0) {
		return 0;
	    } else if (dirY < 0) {
		return -1;
	    } else {
		return -1;
	    }
	} else {
	    if (dirY > 0) {
		return -1;
	    } else if (dirY < 0) {
		return 1;
	    } else {
		return 0;
	    }
	}
    }

    public static final int rotate45LeftY(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0) {
		return 0;
	    } else if (dirY < 0) {
		return -1;
	    } else {
		return -1;
	    }
	} else if (dirX < 0) {
	    if (dirY > 0) {
		return 1;
	    } else if (dirY < 0) {
		return 0;
	    } else {
		return 1;
	    }
	} else {
	    if (dirY > 0) {
		return 1;
	    } else if (dirY < 0) {
		return -1;
	    } else {
		return 0;
	    }
	}
    }

    public static final int rotate45RightX(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0) {
		return 0;
	    } else if (dirY < 0) {
		return 1;
	    } else {
		return 1;
	    }
	} else if (dirX < 0) {
	    if (dirY > 0) {
		return -1;
	    } else if (dirY < 0) {
		return 0;
	    } else {
		return -1;
	    }
	} else {
	    if (dirY > 0) {
		return 1;
	    } else if (dirY < 0) {
		return -1;
	    } else {
		return 0;
	    }
	}
    }

    public static final int rotate45RightY(final int dirX, final int dirY) {
	if (dirX > 0) {
	    if (dirY > 0) {
		return 1;
	    } else if (dirY < 0) {
		return 0;
	    } else {
		return 1;
	    }
	} else if (dirX < 0) {
	    if (dirY > 0) {
		return 0;
	    } else if (dirY < 0) {
		return -1;
	    } else {
		return -1;
	    }
	} else {
	    if (dirY > 0) {
		return 1;
	    } else if (dirY < 0) {
		return -1;
	    } else {
		return 0;
	    }
	}
    }
}
