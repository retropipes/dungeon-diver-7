/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.names.Zones;
import com.puttysoftware.images.BufferedImageIcon;

public class CrystalImageManager {
    private static final String LOAD_PATH_SUFFIX = "crystal_";
    private static Class<?> LOAD_CLASS = CrystalImageManager.class;

    public static BufferedImageIcon getImage(final int ID) {
	// Get it from the cache
	final String name = Zones.getZoneNumber(ID);
	return CrystalImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
	try {
	    final URL url = CrystalImageManager.LOAD_CLASS
		    .getResource(Strings.untranslated(Untranslated.ITEM_IMAGE_LOAD_PATH)
			    + CrystalImageManager.LOAD_PATH_SUFFIX + name + Strings.fileExtension(FileExtension.IMAGE));
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	}
    }
}
