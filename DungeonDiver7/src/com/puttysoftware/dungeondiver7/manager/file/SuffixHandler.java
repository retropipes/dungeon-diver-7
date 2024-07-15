/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.game.FileHooks;

public class SuffixHandler implements AbstractSuffixIO {
	@Override
	public void readSuffix(final DataIOReader reader, final int formatVersion) throws IOException {
		DungeonDiver7.getStuffBag().getGameLogic();
		FileHooks.loadGameHook(reader);
	}

	@Override
	public void writeSuffix(final DataIOWriter writer) throws IOException {
		DungeonDiver7.getStuffBag().getGameLogic();
		FileHooks.saveGameHook(writer);
	}
}
