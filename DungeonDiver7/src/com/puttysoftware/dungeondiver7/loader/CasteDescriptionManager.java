/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.caste.CasteConstants;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.diane.fileio.utility.ResourceStreamReader;

public class CasteDescriptionManager {
    public static String getCasteDescription(final int c) {
        final var name = CasteConstants.CASTE_NAMES[c].toLowerCase();
        try (final var rsr = new ResourceStreamReader(CasteDescriptionManager.class.getResourceAsStream(
                "/assets/descriptions/caste/" + name + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
            return rsr.readString();
        } catch (final IOException e) {
            DungeonDiver7.logError(e);
            return null;
        }
    }
}
