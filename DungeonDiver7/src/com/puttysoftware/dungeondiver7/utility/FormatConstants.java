/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public class FormatConstants {
    private static final int DUNGEON_FORMAT_5 = 5;
    private static final int DUNGEON_FORMAT_6 = 6;
    private static final int DUNGEON_FORMAT_7 = 7;
    private static final int DUNGEON_FORMAT_8 = 8;
    private static final int DUNGEON_FORMAT_9 = 9;
    private static final int DUNGEON_FORMAT_10 = 10;
    private static final int DUNGEON_FORMAT_11 = 11;
    private static final int DUNGEON_FORMAT_12 = 12;
    private static final int DUNGEON_FORMAT_15 = 15;
    private static final int DUNGEON_FORMAT_16 = 16;
    private static final int DUNGEON_FORMAT_17 = 17;
    private static final int DUNGEON_FORMAT_18 = 18;
    public static final int DUNGEON_FORMAT_LATEST = 18;
    public static final byte CHARACTER_FORMAT_2 = 2;
    public static final byte CHARACTER_FORMAT_LATEST = 3;

    private FormatConstants() {
	// Do nothing
    }

    public static final boolean isMoveShootAllowed(final int ver) {
	return ver >= FormatConstants.DUNGEON_FORMAT_11;
    }

    public static final boolean isLevelListStored(final int ver) {
	return ver >= FormatConstants.DUNGEON_FORMAT_17;
    }

    public static final boolean isFormatVersionValidGeneration7(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_18;
    }

    public static final boolean isFormatVersionValidGeneration6(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_17;
    }

    public static final boolean isFormatVersionValidGeneration5(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_12 || ver == FormatConstants.DUNGEON_FORMAT_15
		|| ver == FormatConstants.DUNGEON_FORMAT_16;
    }

    public static final boolean isFormatVersionValidGeneration4(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_10 || ver == FormatConstants.DUNGEON_FORMAT_11;
    }

    public static final boolean isFormatVersionValidGeneration3(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_9;
    }

    public static final boolean isFormatVersionValidGeneration2(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_7 || ver == FormatConstants.DUNGEON_FORMAT_8;
    }

    public static final boolean isFormatVersionValidGeneration1(final int ver) {
	return ver == FormatConstants.DUNGEON_FORMAT_5 || ver == FormatConstants.DUNGEON_FORMAT_6;
    }
}
