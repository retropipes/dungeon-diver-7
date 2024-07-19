/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.asset;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public class BattleImageManager {
	private static Class<?> LOAD_CLASS = BattleImageManager.class;

	public static int getGraphicSize() {
		return 64;
	}

	/**
	 *
	 * @param name
	 * @param baseID
	 * @param transformColor
	 * @return
	 */
	public static BufferedImageIcon getImage(final String name, final int baseID) {
		// Get it from the cache
		final var baseName = ObjectImageConstants.getObjectImageName(baseID);
		return BattleImageCache.getCachedImage(name, baseName);
	}

	static BufferedImageIcon getUncachedImage(final String name) {
		try {
			final var url = BattleImageManager.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.OBJECT_IMAGE_LOAD_PATH) + name
							+ Strings.fileExtension(FileExtension.IMAGE));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			return null;
		}
	}
}
