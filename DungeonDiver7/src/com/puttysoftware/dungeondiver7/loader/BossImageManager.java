/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.retropipes.diane.asset.image.BufferedImageIcon;

import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.names.Monsters;

public class BossImageManager {
	private static Class<?> LOAD_CLASS = BossImageManager.class;

	public static BufferedImageIcon getBossImage(final int zoneID) {
		// Get it from the cache
		return BossImageCache.getCachedImage(Monsters.getImageFilename(zoneID));
	}

	public static BufferedImageIcon getFinalBossImage() {
		// Get it from the cache
		return BossImageCache.getCachedImage("chrys");
	}

	static BufferedImageIcon getUncachedImage(final String name) {
		try {
			final var url = BossImageManager.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.BOSS_IMAGE_LOAD_PATH) + name
							+ Strings.fileExtension(FileExtension.IMAGE));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			return null;
		}
	}
}
