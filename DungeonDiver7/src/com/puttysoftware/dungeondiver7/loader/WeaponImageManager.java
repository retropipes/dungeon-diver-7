/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.diane.assets.image.BufferedImageIcon;

public class WeaponImageManager {
    private static final String LOAD_PATH_SUFFIX = "weapon_";
    private static Class<?> LOAD_CLASS = WeaponImageManager.class;

    public static BufferedImageIcon getImage(final int typeID, final int zoneID) {
        // Get it from the cache
        final var name = WeaponImageManager.LOAD_PATH_SUFFIX + Integer.toString(zoneID);
        return WeaponImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            final var url = WeaponImageManager.LOAD_CLASS
                    .getResource(Strings.untranslated(Untranslated.ITEM_IMAGE_LOAD_PATH)
                            + WeaponImageManager.LOAD_PATH_SUFFIX + name + Strings.fileExtension(FileExtension.IMAGE));
            final var image = ImageIO.read(url);
            return new BufferedImageIcon(image);
        } catch (final IOException ie) {
            return null;
        }
    }
}
