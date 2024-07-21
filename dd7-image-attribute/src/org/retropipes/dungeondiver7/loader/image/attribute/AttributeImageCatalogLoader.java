package org.retropipes.dungeondiver7.loader.image.attribute;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.ResourceStreamReader;

class AttributeImageCatalogLoader {
	private static ArrayList<String> ATTRIBUTE_IMAGE_FILENAMES = null;

	static String getAttributeImageFilename(final int index) {
		if (AttributeImageCatalogLoader.ATTRIBUTE_IMAGE_FILENAMES == null) {
			try (final var rsr = new ResourceStreamReader(
					AttributeImageCatalogLoader.class.getResourceAsStream("/asset/catalog/image/attribute.catalog"))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				AttributeImageCatalogLoader.ATTRIBUTE_IMAGE_FILENAMES = rawData;
			} catch (final IOException e) {
				Diane.handleError(e);
				return null;
			}
		}
		if (AttributeImageCatalogLoader.ATTRIBUTE_IMAGE_FILENAMES == null) {
			return null;
		}
		return AttributeImageCatalogLoader.ATTRIBUTE_IMAGE_FILENAMES.get(index);
	}

	private AttributeImageCatalogLoader() {
		// Do nothing
	}
}
