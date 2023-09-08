/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.diane.random.RandomLongRange;

public class IDGenerator {
    public static String generateRandomFilename() {
	return Long.toString(RandomLongRange.generateRaw(), 36).toLowerCase();
    }

    // Constructor
    private IDGenerator() {
	// Do nothing
    }
}