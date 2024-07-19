/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.loader;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.names.Zones;

public class CrystalImageManager {
	private static final String LOAD_PATH_SUFFIX = "crystal_";
	private static Class<?> LOAD_CLASS = CrystalImageManager.class;

	public static BufferedImageIcon getImage(final int ID) {
		// Get it from the cache
		final var name = Zones.getZoneNumber(ID);
		return CrystalImageCache.getCachedImage(name);
	}

	static BufferedImageIcon getUncachedImage(final String name) {
		try {
			final var url = CrystalImageManager.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.ITEM_IMAGE_LOAD_PATH)
							+ CrystalImageManager.LOAD_PATH_SUFFIX + name + Strings.fileExtension(FileExtension.IMAGE));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			return null;
		}
	}
}
