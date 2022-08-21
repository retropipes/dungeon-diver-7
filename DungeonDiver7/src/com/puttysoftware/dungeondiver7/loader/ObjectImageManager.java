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
import com.puttysoftware.images.BufferedImageIcon;

public class ObjectImageManager {
    private static final String DEFAULT_LOAD_PATH = Strings.untranslated(Untranslated.OBJECT_IMAGE_LOAD_PATH);
    private static String LOAD_PATH = ObjectImageManager.DEFAULT_LOAD_PATH;
    private static Class<?> LOAD_CLASS = ObjectImageManager.class;

    /**
     *
     * @param name
     * @param baseID
     * @param transformColor
     * @return
     */
    public static BufferedImageIcon getImage(final String name, final int baseID) {
	// Get it from the cache
	final String baseName = ObjectImageConstants.getObjectImageName(baseID);
	return ObjectImageCache.getCachedImage(name, baseName);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
	try {
	    final URL url = ObjectImageManager.LOAD_CLASS
		    .getResource(ObjectImageManager.LOAD_PATH + name + Strings.fileExtension(FileExtension.IMAGE));
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	}
    }
}
