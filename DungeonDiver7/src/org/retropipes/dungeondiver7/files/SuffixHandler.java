/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.files;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.game.FileHooks;
import org.retropipes.dungeondiver7.settings.Settings;

public class SuffixHandler implements AbstractSuffixIO {
    @Override
    public void readSuffix(final DataIOReader reader, final int formatVersion) throws IOException {
	DungeonDiver7.getStuffBag().getGame();
	FileHooks.loadGameHook(reader, Settings.getGameDifficulty());
    }

    @Override
    public void writeSuffix(final DataIOWriter writer) throws IOException {
	DungeonDiver7.getStuffBag().getGame();
	FileHooks.saveGameHook(writer);
    }
}
