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

public class ArmorImageManager {
	private static final String LOAD_PATH_SUFFIX = "_";
	private static Class<?> LOAD_CLASS = ArmorImageManager.class;

	public static BufferedImageIcon getImage(final int typeID, final int zoneID) {
		// Get it from the cache
		final var name = Strings.allArmorTypes()[typeID] + ArmorImageManager.LOAD_PATH_SUFFIX
				+ Integer.toString(zoneID);
		return ArmorImageCache.getCachedImage(name);
	}

	static BufferedImageIcon getUncachedImage(final String name) {
		try {
			final var url = ArmorImageManager.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.ITEM_IMAGE_LOAD_PATH)
							+ ArmorImageManager.LOAD_PATH_SUFFIX + name + Strings.fileExtension(FileExtension.IMAGE));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			return null;
		}
	}
}
