/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.dungeon;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.dungeon.AbstractSuffixIO;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.game.FileHooks;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public class SuffixHandler implements AbstractSuffixIO {
    @Override
    public void readSuffix(final FileIOReader reader, final int formatVersion) throws IOException {
	Integration1.getApplication().getGameLogic();
	FileHooks.loadGameHook(reader);
    }

    @Override
    public void writeSuffix(final FileIOWriter writer) throws IOException {
	Integration1.getApplication().getGameLogic();
	FileHooks.saveGameHook(writer);
    }
}
