/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

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
