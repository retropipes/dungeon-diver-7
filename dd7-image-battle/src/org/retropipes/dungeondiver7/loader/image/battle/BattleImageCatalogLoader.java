package org.retropipes.dungeondiver7.loader.image.battle;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.ResourceStreamReader;

class BattleImageCatalogLoader {
	private static ArrayList<String> BATTLE_IMAGE_FILENAMES = null;

	static String getBattleImageFilename(final int index) {
		if (BattleImageCatalogLoader.BATTLE_IMAGE_FILENAMES == null) {
			try (final var rsr = new ResourceStreamReader(
					BattleImageCatalogLoader.class.getResourceAsStream("/asset/catalog/image/battle.catalog"))) {
				// Fetch data
				final var rawData = new ArrayList<String>();
				var line = "";
				while (line != null) {
					line = rsr.readString();
					if (line != null) {
						rawData.add(line);
					}
				}
				BattleImageCatalogLoader.BATTLE_IMAGE_FILENAMES = rawData;
			} catch (final IOException e) {
				Diane.handleError(e);
				return null;
			}
		}
		if (BattleImageCatalogLoader.BATTLE_IMAGE_FILENAMES == null) {
			return null;
		}
		return BattleImageCatalogLoader.BATTLE_IMAGE_FILENAMES.get(index);
	}

	private BattleImageCatalogLoader() {
		// Do nothing
	}
}
