package org.retropipes.dungeondiver7.loader.image.gameobject;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.ResourceStreamReader;

class ObjectImageCatalogLoader {
	private static ArrayList<String> FILENAME_CACHE = null;
	private static String CATALOG_PATH = "/asset/catalog/image/object.catalog";

	static String getFilename(final int index) {
		if (ObjectImageCatalogLoader.FILENAME_CACHE == null) {
			try (final var rsr = new ResourceStreamReader(
					ObjectImageCatalogLoader.class.getResourceAsStream(CATALOG_PATH))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				ObjectImageCatalogLoader.FILENAME_CACHE = rawData;
			} catch (final IOException e) {
				Diane.handleError(e);
				return null;
			}
		}
		if (ObjectImageCatalogLoader.FILENAME_CACHE == null) {
			return null;
		}
		return ObjectImageCatalogLoader.FILENAME_CACHE.get(index);
	}

	private ObjectImageCatalogLoader() {
		// Do nothing
	}
}
