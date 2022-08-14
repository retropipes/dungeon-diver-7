/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.caste.CasteConstants;
import com.puttysoftware.dungeondiver7.utility.FileExtensions;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class CasteDescriptionManager {
    public static String getCasteDescription(final int c) {
	final String name = CasteConstants.CASTE_NAMES[c].toLowerCase();
	try (final ResourceStreamReader rsr = new ResourceStreamReader(
		CasteDescriptionManager.class.getResourceAsStream(
			"/assets/descriptions/caste/" + name + FileExtensions.getInternalDataExtensionWithPeriod()))) {
	    // Fetch description
	    final String desc = rsr.readString();
	    return desc;
	} catch (final IOException e) {
	    DungeonDiver7.getErrorLogger().logError(e);
	    return null;
	}
    }
}
