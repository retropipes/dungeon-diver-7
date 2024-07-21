package org.retropipes.dungeondiver7.loader.image.battle;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.ResourceStreamReader;

class BattleImageCatalogLoader {

	private static ArrayList<String> FILENAME_CACHE = null;
	private static String CATALOG_PATH = "/asset/catalog/image/battle.catalog";

	static String getFilename(final int index) {
		if (FILENAME_CACHE == null) {
			try (final var rsr = new ResourceStreamReader(
					BattleImageCatalogLoader.class.getResourceAsStream(CATALOG_PATH))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				FILENAME_CACHE = rawData;
			} catch (final IOException e) {
				Diane.handleError(e);
				return null;
			}
		}
		if (FILENAME_CACHE == null) {
			return null;
		}
		return FILENAME_CACHE.get(index);
	}

	private BattleImageCatalogLoader() {
		// Do nothing
	}
}
