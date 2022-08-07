/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.names.MonsterNames;
import com.puttysoftware.dungeondiver7.names.ZoneNames;
import com.puttysoftware.images.BufferedImageIcon;

public class MonsterImageManager {
    private static final String DEFAULT_LOAD_PATH = "/assets/images/monsters/";
    private static String LOAD_PATH = MonsterImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = MonsterImageManager.class;

    public static BufferedImageIcon getImage(final int zoneID, final int monID) {
	// Get it from the cache
	final String name = ZoneNames.getZoneName(zoneID) + MonsterNames.getName(monID);
	return MonsterImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
	try {
	    final URL url = MonsterImageManager.LOAD_CLASS.getResource(MonsterImageManager.LOAD_PATH + name + ".png");
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	}
    }
}
