/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.integration1.names.ZoneNames;
import com.puttysoftware.images.BufferedImageIcon;

public class CrystalImageManager {
    private static final String DEFAULT_LOAD_PATH = "/assets/images/items/crystals/";
    private static String LOAD_PATH = CrystalImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = CrystalImageManager.class;

    public static BufferedImageIcon getImage(final int ID) {
	// Get it from the cache
	final String name = ZoneNames.getZoneName(ID);
	return CrystalImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
	try {
	    final URL url = CrystalImageManager.LOAD_CLASS.getResource(CrystalImageManager.LOAD_PATH + name + ".png");
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	}
    }
}
