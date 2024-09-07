/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.files;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.game.FileHooks;
import org.retropipes.dungeondiver7.prefs.Prefs;

public class SuffixHandler implements AbstractSuffixIO {
    @Override
    public void readSuffix(final DataIOReader reader, final int formatVersion) throws IOException {
	DungeonDiver7.getStuffBag().getGame();
	FileHooks.loadGameHook(reader, Prefs.getGameDifficulty());
    }

    @Override
    public void writeSuffix(final DataIOWriter writer) throws IOException {
	DungeonDiver7.getStuffBag().getGame();
	FileHooks.saveGameHook(writer);
    }
}
