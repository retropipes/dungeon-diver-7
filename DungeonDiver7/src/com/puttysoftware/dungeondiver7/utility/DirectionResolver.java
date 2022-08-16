/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;

public class DirectionResolver {
    private DirectionResolver() {
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

    public static Direction resolveRelativeDirection(final int dX, final int dY) {
	final int dirX = (int) Math.signum(dX);
	final int dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Direction.NORTH;
	} else if (dirX == 0 && dirY == 1) {
	    return Direction.SOUTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Direction.WEST;
	} else if (dirX == 1 && dirY == 0) {
	    return Direction.EAST;
	} else if (dirX == 1 && dirY == 1) {
	    return Direction.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Direction.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Direction.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Direction.NORTHEAST;
	} else {
	    return Direction.INVALID;
	}
    }

    public static Direction resolveRelativeDirectionInvert(final int dX, final int dY) {
	final int dirX = (int) Math.signum(dX);
	final int dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Direction.SOUTH;
	} else if (dirX == 0 && dirY == 1) {
	    return Direction.NORTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Direction.EAST;
	} else if (dirX == 1 && dirY == 0) {
	    return Direction.WEST;
	} else if (dirX == 1 && dirY == 1) {
	    return Direction.NORTHWEST;
	} else if (dirX == -1 && dirY == 1) {
	    return Direction.NORTHEAST;
	} else if (dirX == -1 && dirY == -1) {
	    return Direction.SOUTHEAST;
	} else if (dirX == 1 && dirY == -1) {
	    return Direction.SOUTHWEST;
	} else {
	    return Direction.INVALID;
	}
    }

    public static Direction resolveRelativeDirectionHV(final int dX, final int dY) {
	final int dirX = (int) Math.signum(dX);
	final int dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Direction.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Direction.VERTICAL;
	} else if (dirX == 0 && dirY == 1) {
	    return Direction.VERTICAL;
	} else if (dirX == -1 && dirY == 0) {
	    return Direction.HORIZONTAL;
	} else if (dirX == 1 && dirY == 0) {
	    return Direction.HORIZONTAL;
	} else if (dirX == 1 && dirY == 1) {
	    return Direction.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Direction.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Direction.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Direction.NORTHEAST;
	} else {
	    return Direction.INVALID;
	}
    }

    public static int[] unresolveRelativeDirection(final Direction dir) {
	int[] res = new int[2];
	if (dir == Direction.NONE) {
	    res[0] = 0;
	    res[1] = 0;
	} else if (dir == Direction.NORTH) {
	    res[0] = 0;
	    res[1] = -1;
	} else if (dir == Direction.SOUTH) {
	    res[0] = 0;
	    res[1] = 1;
	} else if (dir == Direction.WEST) {
	    res[0] = -1;
	    res[1] = 0;
	} else if (dir == Direction.EAST) {
	    res[0] = 1;
	    res[1] = 0;
	} else if (dir == Direction.SOUTHEAST) {
	    res[0] = 1;
	    res[1] = 1;
	} else if (dir == Direction.SOUTHWEST) {
	    res[0] = -1;
	    res[1] = 1;
	} else if (dir == Direction.NORTHWEST) {
	    res[0] = -1;
	    res[1] = -1;
	} else if (dir == Direction.NORTHEAST) {
	    res[0] = 1;
	    res[1] = -1;
	} else {
	    res = null;
	}
	return res;
    }

    public static final String resolveDirectionConstantToName(final Direction d) {
	switch (d) {
	case EAST:
	    return DirectionName.EAST;
	case HORIZONTAL:
	    return DirectionName.HORIZONTAL;
	case INVALID:
	    return DirectionName.INVALID;
	case NONE:
	    return DirectionName.NONE;
	case NORTH:
	    return DirectionName.NORTH;
	case NORTHEAST:
	    return DirectionName.NORTHEAST;
	case NORTHWEST:
	    return DirectionName.NORTHWEST;
	case SOUTH:
	    return DirectionName.SOUTH;
	case SOUTHEAST:
	    return DirectionName.SOUTHEAST;
	case SOUTHWEST:
	    return DirectionName.SOUTHWEST;
	case VERTICAL:
	    return DirectionName.VERTICAL;
	case WEST:
	    return DirectionName.WEST;
	default:
	    return DirectionName.INVALID;
	}
    }

    public static String resolveDirectionConstantToImageName(final Direction dir) {
	if (dir == null) {
	    return LocaleConstants.COMMON_STRING_EMPTY;
	}
	return LocaleLoader.loadString(LocaleConstants.STRINGS_FILE, dir.getInternalValue());
    }
}
