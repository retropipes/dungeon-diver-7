/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class ImageDataManager {
    public static String[] getObjectGraphicsData() {
	try (final ResourceStreamReader rsr = new ResourceStreamReader(
		ImageDataManager.class.getResourceAsStream("/assets/data/images/objects.txt"))) {
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
	    DungeonDiver7.logError(e);
	    return null;
	}
    }

    public static String[] getStatGraphicsData() {
	try (final ResourceStreamReader rsr = new ResourceStreamReader(
		ImageDataManager.class.getResourceAsStream("/assets/data/images/stats.txt"))) {
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
	    DungeonDiver7.logError(e);
	    return null;
	}
    }
}
