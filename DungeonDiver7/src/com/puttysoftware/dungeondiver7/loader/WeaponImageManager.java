/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.locale.GlobalFile;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.names.ZoneNames;
import com.puttysoftware.images.BufferedImageIcon;

public class WeaponImageManager {
    private static final String DEFAULT_LOAD_PATH = "/assets/images/items/weapons/";
    private static String LOAD_PATH = WeaponImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = WeaponImageManager.class;

    public static BufferedImageIcon getImage(final int typeID, final int zoneID) {
	// Get it from the cache
	final String name = LocaleLoader.loadGlobalString(GlobalFile.WEAPON_TYPES, typeID) + "/"
		+ ZoneNames.getZoneNumber(zoneID);
	return WeaponImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
	try {
	    final URL url = WeaponImageManager.LOAD_CLASS.getResource(WeaponImageManager.LOAD_PATH + name + ".png");
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	}
    }
}
