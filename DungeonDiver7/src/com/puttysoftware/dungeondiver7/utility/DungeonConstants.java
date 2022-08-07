/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;

public class DungeonConstants {
    public static final int LAYER_LOWER_GROUND = 0;
    public static final int LAYER_UPPER_GROUND = 1;
    public static final int LAYER_LOWER_OBJECTS = 2;
    public static final int LAYER_UPPER_OBJECTS = 3;
    public static final int NUM_LAYERS = 4;
    public static final int LAYER_VIRTUAL = 0;
    public static final int LAYER_VIRTUAL_CHARACTER = 1;
    public static final int NUM_VIRTUAL_LAYERS = 2;
    private static String[] LAYER_LIST = null;
    public static final int ERA_DISTANT_PAST = 0;
    public static final int ERA_PAST = 1;
    public static final int ERA_PRESENT = 2;
    public static final int ERA_FUTURE = 3;
    public static final int ERA_DISTANT_FUTURE = 4;
    private static String[] ERA_LIST = null;
    public static final int PLAYER_DIMS = 3;
    public static final int NUM_PLAYERS = 9;

    private DungeonConstants() {
	// Do nothing
    }

    public static void activeLanguageChanged() {
	DungeonConstants.LAYER_LIST = new String[] {
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_LOWER_GROUND_LAYER),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_UPPER_GROUND_LAYER),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_LOWER_OBJECTS_LAYER),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_UPPER_OBJECTS_LAYER) };
	DungeonConstants.ERA_LIST = new String[] {
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_PAST),
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PAST),
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PRESENT),
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_FUTURE),
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_FUTURE) };
    }

    public static String[] getLayerList() {
	return DungeonConstants.LAYER_LIST;
    }

    public static String[] getEraList() {
	return DungeonConstants.ERA_LIST;
    }

    public static Direction nextDirOrtho(final Direction input) {
	switch (input) {
	case INVALID:
	    return Direction.INVALID;
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.EAST;
	case NORTHEAST:
	    return Direction.SOUTHEAST;
	case EAST:
	    return Direction.SOUTH;
	case SOUTHEAST:
	    return Direction.SOUTHWEST;
	case SOUTH:
	    return Direction.WEST;
	case SOUTHWEST:
	    return Direction.NORTHWEST;
	case WEST:
	    return Direction.NORTH;
	case NORTHWEST:
	    return Direction.NORTHEAST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	default:
	    return Direction.INVALID;
	}
    }

    public static Direction previousDirOrtho(final Direction input) {
	switch (input) {
	case INVALID:
	    return Direction.INVALID;
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.WEST;
	case NORTHEAST:
	    return Direction.NORTHWEST;
	case EAST:
	    return Direction.NORTH;
	case SOUTHEAST:
	    return Direction.NORTHEAST;
	case SOUTH:
	    return Direction.EAST;
	case SOUTHWEST:
	    return Direction.SOUTHEAST;
	case WEST:
	    return Direction.SOUTH;
	case NORTHWEST:
	    return Direction.SOUTHWEST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	default:
	    return Direction.INVALID;
	}
    }

    public static Direction nextDir(final Direction input) {
	switch (input) {
	case INVALID:
	    return Direction.INVALID;
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.NORTHEAST;
	case NORTHEAST:
	    return Direction.EAST;
	case EAST:
	    return Direction.SOUTHEAST;
	case SOUTHEAST:
	    return Direction.SOUTH;
	case SOUTH:
	    return Direction.SOUTHWEST;
	case SOUTHWEST:
	    return Direction.WEST;
	case WEST:
	    return Direction.NORTHWEST;
	case NORTHWEST:
	    return Direction.NORTH;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	default:
	    return Direction.INVALID;
	}
    }

    public static Direction previousDir(final Direction input) {
	switch (input) {
	case INVALID:
	    return Direction.INVALID;
	case NONE:
	    return Direction.NONE;
	case NORTH:
	    return Direction.NORTHWEST;
	case NORTHEAST:
	    return Direction.NORTH;
	case EAST:
	    return Direction.NORTHEAST;
	case SOUTHEAST:
	    return Direction.EAST;
	case SOUTH:
	    return Direction.SOUTHEAST;
	case SOUTHWEST:
	    return Direction.SOUTH;
	case WEST:
	    return Direction.SOUTHWEST;
	case NORTHWEST:
	    return Direction.WEST;
	case HORIZONTAL:
	    return Direction.VERTICAL;
	case VERTICAL:
	    return Direction.HORIZONTAL;
	default:
	    return Direction.INVALID;
	}
    }
}
