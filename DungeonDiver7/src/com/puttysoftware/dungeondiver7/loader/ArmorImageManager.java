/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.locale.old.GlobalFile;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.dungeondiver7.names.Zones;
import com.puttysoftware.images.BufferedImageIcon;

public class ArmorImageManager {
    private static final String DEFAULT_LOAD_PATH = "/assets/images/items/armor/";
    private static String LOAD_PATH = ArmorImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = ArmorImageManager.class;

    public static BufferedImageIcon getImage(final int typeID, final int zoneID) {
	// Get it from the cache
	final String name = LocaleLoader.loadGlobalString(GlobalFile.ARMOR_TYPES, typeID) + "/"
		+ Zones.getZoneNumber(zoneID);
	return ArmorImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
	try {
	    final URL url = ArmorImageManager.LOAD_CLASS.getResource(ArmorImageManager.LOAD_PATH + name + ".png");
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	}
    }
}
