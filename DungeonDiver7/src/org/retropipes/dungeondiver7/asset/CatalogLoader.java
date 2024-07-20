package org.retropipes.dungeondiver7.asset;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.fileio.utility.ResourceStreamReader;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.Music;

public class CatalogLoader {
	private static ArrayList<String> OBJECT_IMAGE_FILENAMES = null;
	private static ArrayList<String> MUSIC_FILENAMES = null;

	public static String getObjectImageFilename(final int index) {
		if (CatalogLoader.OBJECT_IMAGE_FILENAMES == null) {
			try (final var rsr = new ResourceStreamReader(
					CatalogLoader.class.getResourceAsStream("/asset/catalog/image/object.catalog"))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				CatalogLoader.OBJECT_IMAGE_FILENAMES = rawData;
			} catch (final IOException e) {
				DungeonDiver7.logError(e);
				return null;
			}
		}
		if (CatalogLoader.OBJECT_IMAGE_FILENAMES == null) {
			return null;
		}
		return CatalogLoader.OBJECT_IMAGE_FILENAMES.get(index);
	}

	public static String getMusicFilename(final Music music) {
		if (CatalogLoader.MUSIC_FILENAMES == null) {
			try (final var rsr = new ResourceStreamReader(
					CatalogLoader.class.getResourceAsStream("/asset/catalog/music.catalog"))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				CatalogLoader.MUSIC_FILENAMES = rawData;
			} catch (final IOException e) {
				DungeonDiver7.logError(e);
				return null;
			}
		}
		if (CatalogLoader.MUSIC_FILENAMES == null) {
			return null;
		}
		return CatalogLoader.MUSIC_FILENAMES.get(music.ordinal());
	}

	private CatalogLoader() {
		// Do nothing
	}
}
