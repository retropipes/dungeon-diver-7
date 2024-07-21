/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.asset;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.StatusImage;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public class StatImageManager {
	private static Class<?> LOAD_CLASS = StatImageManager.class;

	static BufferedImageIcon getUncachedImage(final String name) {
		try {
			final var url = StatImageManager.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.STAT_IMAGE_LOAD_PATH) + name
							+ Strings.fileExtension(FileExtension.IMAGE));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			return null;
		}
	}

	public static BufferedImageIcon load(final StatusImage image) {
		// Get it from the cache
		final var name = Strings.statusImage(image);
		return StatImageCache.getCachedImage(name);
	}
}