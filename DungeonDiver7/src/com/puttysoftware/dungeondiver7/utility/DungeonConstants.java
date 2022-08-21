/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.locale.EditorString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.TimeTravel;

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
	DungeonConstants.LAYER_LIST = new String[] { Strings.editor(EditorString.LOWER_GROUND_LAYER),
		Strings.editor(EditorString.UPPER_GROUND_LAYER), Strings.editor(EditorString.LOWER_OBJECTS_LAYER),
		Strings.editor(EditorString.UPPER_OBJECTS_LAYER) };
	DungeonConstants.ERA_LIST = new String[] { Strings.timeTravel(TimeTravel.FAR_PAST),
		Strings.timeTravel(TimeTravel.PAST), Strings.timeTravel(TimeTravel.PRESENT),
		Strings.timeTravel(TimeTravel.FUTURE), Strings.timeTravel(TimeTravel.FAR_FUTURE) };
    }

    public static String[] getLayerList() {
	return DungeonConstants.LAYER_LIST;
    }

    public static String[] getEraList() {
	return DungeonConstants.ERA_LIST;
    }

    public static Directions nextDirOrtho(final Directions input) {
	switch (input) {
	case INVALID:
	    return Directions.INVALID;
	case NONE:
	    return Directions.NONE;
	case NORTH:
	    return Directions.EAST;
	case NORTHEAST:
	    return Directions.SOUTHEAST;
	case EAST:
	    return Directions.SOUTH;
	case SOUTHEAST:
	    return Directions.SOUTHWEST;
	case SOUTH:
	    return Directions.WEST;
	case SOUTHWEST:
	    return Directions.NORTHWEST;
	case WEST:
	    return Directions.NORTH;
	case NORTHWEST:
	    return Directions.NORTHEAST;
	case HORIZONTAL:
	    return Directions.VERTICAL;
	case VERTICAL:
	    return Directions.HORIZONTAL;
	default:
	    return Directions.INVALID;
	}
    }

    public static Directions previousDirOrtho(final Directions input) {
	switch (input) {
	case INVALID:
	    return Directions.INVALID;
	case NONE:
	    return Directions.NONE;
	case NORTH:
	    return Directions.WEST;
	case NORTHEAST:
	    return Directions.NORTHWEST;
	case EAST:
	    return Directions.NORTH;
	case SOUTHEAST:
	    return Directions.NORTHEAST;
	case SOUTH:
	    return Directions.EAST;
	case SOUTHWEST:
	    return Directions.SOUTHEAST;
	case WEST:
	    return Directions.SOUTH;
	case NORTHWEST:
	    return Directions.SOUTHWEST;
	case HORIZONTAL:
	    return Directions.VERTICAL;
	case VERTICAL:
	    return Directions.HORIZONTAL;
	default:
	    return Directions.INVALID;
	}
    }

    public static Directions nextDir(final Directions input) {
	switch (input) {
	case INVALID:
	    return Directions.INVALID;
	case NONE:
	    return Directions.NONE;
	case NORTH:
	    return Directions.NORTHEAST;
	case NORTHEAST:
	    return Directions.EAST;
	case EAST:
	    return Directions.SOUTHEAST;
	case SOUTHEAST:
	    return Directions.SOUTH;
	case SOUTH:
	    return Directions.SOUTHWEST;
	case SOUTHWEST:
	    return Directions.WEST;
	case WEST:
	    return Directions.NORTHWEST;
	case NORTHWEST:
	    return Directions.NORTH;
	case HORIZONTAL:
	    return Directions.VERTICAL;
	case VERTICAL:
	    return Directions.HORIZONTAL;
	default:
	    return Directions.INVALID;
	}
    }

    public static Directions previousDir(final Directions input) {
	switch (input) {
	case INVALID:
	    return Directions.INVALID;
	case NONE:
	    return Directions.NONE;
	case NORTH:
	    return Directions.NORTHWEST;
	case NORTHEAST:
	    return Directions.NORTH;
	case EAST:
	    return Directions.NORTHEAST;
	case SOUTHEAST:
	    return Directions.EAST;
	case SOUTH:
	    return Directions.SOUTHEAST;
	case SOUTHWEST:
	    return Directions.SOUTH;
	case WEST:
	    return Directions.SOUTHWEST;
	case NORTHWEST:
	    return Directions.WEST;
	case HORIZONTAL:
	    return Directions.VERTICAL;
	case VERTICAL:
	    return Directions.HORIZONTAL;
	default:
	    return Directions.INVALID;
	}
    }
}
