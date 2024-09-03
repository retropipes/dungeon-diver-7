package org.retropipes.dungeondiver7.gameobject;

import org.retropipes.diane.direction.Direction;

class GameObjectDirectionsHelper {
    public static Direction nextDir(final Direction input) {
	return switch (input) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.NORTH_EAST;
	case NORTH_EAST -> Direction.EAST;
	case EAST -> Direction.SOUTH_EAST;
	case SOUTH_EAST -> Direction.SOUTH;
	case SOUTH -> Direction.SOUTH_WEST;
	case SOUTH_WEST -> Direction.WEST;
	case WEST -> Direction.NORTH_WEST;
	case NORTH_WEST -> Direction.NORTH;
	default -> Direction.NONE;
	};
    }

    public static Direction nextDirOrtho(final Direction input) {
	return switch (input) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.EAST;
	case NORTH_EAST -> Direction.SOUTH_EAST;
	case EAST -> Direction.SOUTH;
	case SOUTH_EAST -> Direction.SOUTH_WEST;
	case SOUTH -> Direction.WEST;
	case SOUTH_WEST -> Direction.NORTH_WEST;
	case WEST -> Direction.NORTH;
	case NORTH_WEST -> Direction.NORTH_EAST;
	default -> Direction.NONE;
	};
    }

    public static Direction previousDir(final Direction input) {
	return switch (input) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.NORTH_WEST;
	case NORTH_EAST -> Direction.NORTH;
	case EAST -> Direction.NORTH_EAST;
	case SOUTH_EAST -> Direction.EAST;
	case SOUTH -> Direction.SOUTH_EAST;
	case SOUTH_WEST -> Direction.SOUTH;
	case WEST -> Direction.SOUTH_WEST;
	case NORTH_WEST -> Direction.WEST;
	default -> Direction.NONE;
	};
    }

    public static Direction previousDirOrtho(final Direction input) {
	return switch (input) {
	case NONE -> Direction.NONE;
	case NORTH -> Direction.WEST;
	case NORTH_EAST -> Direction.NORTH_WEST;
	case EAST -> Direction.NORTH;
	case SOUTH_EAST -> Direction.NORTH_EAST;
	case SOUTH -> Direction.EAST;
	case SOUTH_WEST -> Direction.SOUTH_EAST;
	case WEST -> Direction.SOUTH;
	case NORTH_WEST -> Direction.SOUTH_WEST;
	default -> Direction.NONE;
	};
    }
}
