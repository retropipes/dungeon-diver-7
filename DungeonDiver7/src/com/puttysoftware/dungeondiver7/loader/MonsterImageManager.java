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
import com.puttysoftware.dungeondiver7.names.Monsters;
import com.puttysoftware.diane.assets.image.BufferedImageIcon;

public class MonsterImageManager {
    private static Class<?> LOAD_CLASS = MonsterImageManager.class;

    public static BufferedImageIcon getImage(final int zoneID, final int monID) {
        // Get it from the cache
        final var name = Monsters.getImageFilename(monID);
        return MonsterImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            final var url = MonsterImageManager.LOAD_CLASS
                    .getResource(Strings.untranslated(Untranslated.MONSTER_IMAGE_LOAD_PATH) + name
                            + Strings.fileExtension(FileExtension.IMAGE));
            final var image = ImageIO.read(url);
            return new BufferedImageIcon(image);
        } catch (final IOException ie) {
            return null;
        }
    }
}
