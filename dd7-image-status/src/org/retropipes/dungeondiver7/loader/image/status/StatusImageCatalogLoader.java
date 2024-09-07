package org.retropipes.dungeondiver7.loader.image.status;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.ResourceStreamReader;
import org.retropipes.dungeondiver7.locale.Strings;

class StatusImageCatalogLoader {
    private static ArrayList<String> FILENAME_CACHE = null;
    private static String CATALOG_PATH = "/asset/catalog/image/status.catalog"; //$NON-NLS-1$

    static String getFilename(final int index) {
	if (FILENAME_CACHE == null) {
	    try (final var rsr = new ResourceStreamReader(
		    StatusImageCatalogLoader.class.getResourceAsStream(CATALOG_PATH))) {
		// Fetch data
		final var rawData = new ArrayList<String>();
		var line = Strings.EMPTY;
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

    private StatusImageCatalogLoader() {
	// Do nothing
    }
}
