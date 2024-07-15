/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import org.retropipes.diane.direction.Direction;

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

	public static void activeLanguageChanged() {
		DungeonConstants.LAYER_LIST = new String[] { Strings.editor(EditorString.LOWER_GROUND_LAYER),
				Strings.editor(EditorString.UPPER_GROUND_LAYER), Strings.editor(EditorString.LOWER_OBJECTS_LAYER),
				Strings.editor(EditorString.UPPER_OBJECTS_LAYER) };
		DungeonConstants.ERA_LIST = new String[] { Strings.timeTravel(TimeTravel.FAR_PAST),
				Strings.timeTravel(TimeTravel.PAST), Strings.timeTravel(TimeTravel.PRESENT),
				Strings.timeTravel(TimeTravel.FUTURE), Strings.timeTravel(TimeTravel.FAR_FUTURE) };
	}

	public static String[] getEraList() {
		return DungeonConstants.ERA_LIST;
	}

	public static String[] getLayerList() {
		return DungeonConstants.LAYER_LIST;
	}

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

	private DungeonConstants() {
		// Do nothing
	}
}
