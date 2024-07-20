/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.asset;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.fileio.utility.ResourceStreamReader;
import org.retropipes.dungeondiver7.DungeonDiver7;

public class ImageDataManager {
	private static ArrayList<String> OBJECT_IMAGE_FILENAMES = null;

	public static String[] getObjectGraphicsData() {
		try (final var rsr = new ResourceStreamReader(
				ImageDataManager.class.getResourceAsStream("/assets/data/images/objects.txt"))) {
			// Fetch data
			final var rawData = new ArrayList<String>();
			var line = "";
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
		try (final var rsr = new ResourceStreamReader(
				ImageDataManager.class.getResourceAsStream("/assets/data/images/stats.txt"))) {
			// Fetch data
			final var rawData = new ArrayList<String>();
			var line = "";
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

	public static String getObjectImageFilename(int index) {
		if (ImageDataManager.OBJECT_IMAGE_FILENAMES == null) {
			try (final var rsr = new ResourceStreamReader(
					ImageDataManager.class.getResourceAsStream("/asset/catalog/image/object.catalog"))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				ImageDataManager.OBJECT_IMAGE_FILENAMES = rawData;
			} catch (final IOException e) {
				DungeonDiver7.logError(e);
				return null;
			}
		}
		if (ImageDataManager.OBJECT_IMAGE_FILENAMES == null) {
			return null;
		}
		return ImageDataManager.OBJECT_IMAGE_FILENAMES.get(index);
	}
}
