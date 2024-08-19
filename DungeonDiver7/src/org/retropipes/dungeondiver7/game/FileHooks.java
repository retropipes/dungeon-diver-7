/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.game;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.creature.GameDifficulty;
import org.retropipes.dungeondiver7.creature.party.PartyManager;

public class FileHooks {
    public static void loadGameHook(final DataIOReader mapFile, final GameDifficulty diff) throws IOException {
	PartyManager.loadGameHook(mapFile, diff);
    }

    public static void saveGameHook(final DataIOWriter mapFile) throws IOException {
	PartyManager.saveGameHook(mapFile);
    }

    private FileHooks() {
	// Do nothing
    }
}
