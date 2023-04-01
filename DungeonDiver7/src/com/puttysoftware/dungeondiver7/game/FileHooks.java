/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;

public class FileHooks {
    private FileHooks() {
        // Do nothing
    }

    public static void loadGameHook(final DataIOReader mapFile) throws IOException {
        PartyManager.loadGameHook(mapFile);
    }

    public static void saveGameHook(final DataIOWriter mapFile) throws IOException {
        PartyManager.saveGameHook(mapFile);
    }
}
