/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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
