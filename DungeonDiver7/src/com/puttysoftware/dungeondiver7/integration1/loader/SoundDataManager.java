/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.loader;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class SoundDataManager {
    public static String[] getSoundData() {
	try (final ResourceStreamReader rsr = new ResourceStreamReader(
		SoundDataManager.class.getResourceAsStream("/assets/data/sound/sounds.txt"))) {
	    // Fetch data
	    final ArrayList<String> rawData = new ArrayList<>();
	    String line = "";
	    while (line != null) {
		line = rsr.readString();
		if (line != null) {
		    rawData.add(line);
		}
	    }
	    return rawData.toArray(new String[rawData.size()]);
	} catch (final IOException e) {
	    DungeonDiver7.getErrorLogger().logError(e);
	    return null;
	}
    }
}
