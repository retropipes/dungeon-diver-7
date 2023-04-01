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

public class EffectImageManager {
    private static Class<?> LOAD_CLASS = EffectImageManager.class;

    public static BufferedImageIcon getImage(final int imageID) {
        // Get it from the cache
        final var name = EffectImageConstants.getEffectImageName(imageID);
        return EffectImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            final var url = EffectImageManager.LOAD_CLASS
                    .getResource(Strings.untranslated(Untranslated.EFFECT_IMAGE_LOAD_PATH) + name
                            + Strings.fileExtension(FileExtension.IMAGE));
            final var image = ImageIO.read(url);
            return new BufferedImageIcon(image);
        } catch (final IOException ie) {
            return null;
        }
    }
}
