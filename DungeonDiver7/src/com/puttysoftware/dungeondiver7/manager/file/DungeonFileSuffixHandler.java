/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.utility.FormatConstants;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public class DungeonFileSuffixHandler implements AbstractSuffixIO {
    @Override
    public void readSuffix(final FileIOReader reader, final int formatVersion) throws IOException {
	if (FormatConstants.isFormatVersionValidGeneration1(formatVersion)) {
	    DungeonDiver7.getApplication().getGameLogic().loadGameHookG1(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration2(formatVersion)) {
	    DungeonDiver7.getApplication().getGameLogic().loadGameHookG2(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration3(formatVersion)) {
	    DungeonDiver7.getApplication().getGameLogic().loadGameHookG3(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration4(formatVersion)) {
	    DungeonDiver7.getApplication().getGameLogic().loadGameHookG4(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration5(formatVersion)) {
	    DungeonDiver7.getApplication().getGameLogic().loadGameHookG5(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration6(formatVersion)) {
	    DungeonDiver7.getApplication().getGameLogic().loadGameHookG6(reader);
	}
    }

    @Override
    public void writeSuffix(final FileIOWriter writer) throws IOException {
	DungeonDiver7.getApplication().getGameLogic().saveGameHook(writer);
    }
}
