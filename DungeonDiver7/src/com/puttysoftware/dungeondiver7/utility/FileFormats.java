/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public class FileFormats {
	private static final int DUNGEON_5 = 5;
	private static final int DUNGEON_6 = 6;
	private static final int DUNGEON_7 = 7;
	private static final int DUNGEON_8 = 8;
	private static final int DUNGEON_9 = 9;
	private static final int DUNGEON_10 = 10;
	private static final int DUNGEON_11 = 11;
	private static final int DUNGEON_12 = 12;
	private static final int DUNGEON_15 = 15;
	private static final int DUNGEON_16 = 16;
	private static final int DUNGEON_17 = 17;
	private static final int DUNGEON_18 = 18;
	public static final int DUNGEON_LATEST = 18;
	public static final byte CHARACTER_2 = 2;
	public static final byte CHARACTER_LATEST = 3;

	public static final boolean isFormatVersionValidGeneration1(final int ver) {
		return ver == FileFormats.DUNGEON_5 || ver == FileFormats.DUNGEON_6;
	}

	public static final boolean isFormatVersionValidGeneration2(final int ver) {
		return ver == FileFormats.DUNGEON_7 || ver == FileFormats.DUNGEON_8;
	}

	public static final boolean isFormatVersionValidGeneration3(final int ver) {
		return ver == FileFormats.DUNGEON_9;
	}

	public static final boolean isFormatVersionValidGeneration4(final int ver) {
		return ver == FileFormats.DUNGEON_10 || ver == FileFormats.DUNGEON_11;
	}

	public static final boolean isFormatVersionValidGeneration5(final int ver) {
		return ver == FileFormats.DUNGEON_12 || ver == FileFormats.DUNGEON_15 || ver == FileFormats.DUNGEON_16;
	}

	public static final boolean isFormatVersionValidGeneration6(final int ver) {
		return ver == FileFormats.DUNGEON_17;
	}

	public static final boolean isFormatVersionValidGeneration7(final int ver) {
		return ver == FileFormats.DUNGEON_18;
	}

	public static final boolean isLevelListStored(final int ver) {
		return ver >= FileFormats.DUNGEON_17;
	}

	public static final boolean isMoveShootAllowed(final int ver) {
		return ver >= FileFormats.DUNGEON_11;
	}

	private FileFormats() {
		// Do nothing
	}
}
