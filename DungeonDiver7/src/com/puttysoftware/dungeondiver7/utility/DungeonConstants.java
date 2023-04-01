/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.diane.direction.Direction;
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

    public static Direction nextDirOrtho(final Direction input) {
        switch (input) {
            case NONE:
                return Direction.NONE;
            case NORTH:
                return Direction.EAST;
            case NORTH_EAST:
                return Direction.SOUTH_EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH_EAST:
                return Direction.SOUTH_WEST;
            case SOUTH:
                return Direction.WEST;
            case SOUTH_WEST:
                return Direction.NORTH_WEST;
            case WEST:
                return Direction.NORTH;
            case NORTH_WEST:
                return Direction.NORTH_EAST;
            default:
                return Direction.NONE;
        }
    }

    public static Direction previousDirOrtho(final Direction input) {
        switch (input) {
            case NONE:
                return Direction.NONE;
            case NORTH:
                return Direction.WEST;
            case NORTH_EAST:
                return Direction.NORTH_WEST;
            case EAST:
                return Direction.NORTH;
            case SOUTH_EAST:
                return Direction.NORTH_EAST;
            case SOUTH:
                return Direction.EAST;
            case SOUTH_WEST:
                return Direction.SOUTH_EAST;
            case WEST:
                return Direction.SOUTH;
            case NORTH_WEST:
                return Direction.SOUTH_WEST;
            default:
                return Direction.NONE;
        }
    }

    public static Direction nextDir(final Direction input) {
        switch (input) {
            case NONE:
                return Direction.NONE;
            case NORTH:
                return Direction.NORTH_EAST;
            case NORTH_EAST:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH_EAST;
            case SOUTH_EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.SOUTH_WEST;
            case SOUTH_WEST:
                return Direction.WEST;
            case WEST:
                return Direction.NORTH_WEST;
            case NORTH_WEST:
                return Direction.NORTH;
            default:
                return Direction.NONE;
        }
    }

    public static Direction previousDir(final Direction input) {
        switch (input) {
            case NONE:
                return Direction.NONE;
            case NORTH:
                return Direction.NORTH_WEST;
            case NORTH_EAST:
                return Direction.NORTH;
            case EAST:
                return Direction.NORTH_EAST;
            case SOUTH_EAST:
                return Direction.EAST;
            case SOUTH:
                return Direction.SOUTH_EAST;
            case SOUTH_WEST:
                return Direction.SOUTH;
            case WEST:
                return Direction.SOUTH_WEST;
            case NORTH_WEST:
                return Direction.WEST;
            default:
                return Direction.NONE;
        }
    }
}
