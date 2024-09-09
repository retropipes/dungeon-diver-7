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
import org.retropipes.dungeondiver7.utility.FileFormats;

public class DungeonFileSuffixHandler implements AbstractSuffixIO {
    @Override
    public void readSuffix(final DataIOReader reader, final int formatVersion) throws IOException {
	if (FileFormats.isFormatVersionValidGeneration1(formatVersion)) {
	    DungeonDiver7.getStuffBag().getGame().loadGameHookG1(reader);
	} else if (FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
	    DungeonDiver7.getStuffBag().getGame().loadGameHookG2(reader);
	} else if (FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
	    DungeonDiver7.getStuffBag().getGame().loadGameHookG3(reader);
	} else if (FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
	    DungeonDiver7.getStuffBag().getGame().loadGameHookG4(reader);
	} else if (FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
	    DungeonDiver7.getStuffBag().getGame().loadGameHookG5(reader);
	} else if (FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
	    DungeonDiver7.getStuffBag().getGame().loadGameHookG6(reader);
	}
    }

    @Override
    public void writeSuffix(final DataIOWriter writer) throws IOException {
	DungeonDiver7.getStuffBag().getGame().saveGameHook(writer);
    }
}
