/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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