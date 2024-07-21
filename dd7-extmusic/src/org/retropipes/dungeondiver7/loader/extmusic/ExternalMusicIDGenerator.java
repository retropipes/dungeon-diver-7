/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.loader.extmusic;

import org.retropipes.diane.random.RandomLongRange;

public class ExternalMusicIDGenerator {
	public static String generateRandomFilename() {
		return Long.toString(RandomLongRange.generateRaw(), 36).toLowerCase();
	}

	// Constructor
	private ExternalMusicIDGenerator() {
		// Do nothing
	}
}