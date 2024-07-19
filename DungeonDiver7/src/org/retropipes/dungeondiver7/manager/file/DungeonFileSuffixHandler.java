/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.manager.file;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.utility.FileFormats;

public class DungeonFileSuffixHandler implements AbstractSuffixIO {
	@Override
	public void readSuffix(final DataIOReader reader, final int formatVersion) throws IOException {
		if (FileFormats.isFormatVersionValidGeneration1(formatVersion)) {
			DungeonDiver7.getStuffBag().getGameLogic().loadGameHookG1(reader);
		} else if (FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
			DungeonDiver7.getStuffBag().getGameLogic().loadGameHookG2(reader);
		} else if (FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
			DungeonDiver7.getStuffBag().getGameLogic().loadGameHookG3(reader);
		} else if (FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
			DungeonDiver7.getStuffBag().getGameLogic().loadGameHookG4(reader);
		} else if (FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
			DungeonDiver7.getStuffBag().getGameLogic().loadGameHookG5(reader);
		} else if (FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
			DungeonDiver7.getStuffBag().getGameLogic().loadGameHookG6(reader);
		}
	}

	@Override
	public void writeSuffix(final DataIOWriter writer) throws IOException {
		DungeonDiver7.getStuffBag().getGameLogic().saveGameHook(writer);
	}
}
