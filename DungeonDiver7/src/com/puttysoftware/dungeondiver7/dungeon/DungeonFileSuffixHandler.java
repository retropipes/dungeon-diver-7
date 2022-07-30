/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.utilities.FormatConstants;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public class DungeonFileSuffixHandler implements AbstractSuffixIO {
    @Override
    public void readSuffix(final XDataReader reader, final int formatVersion) throws IOException {
	if (FormatConstants.isFormatVersionValidGeneration1(formatVersion)) {
	    DungeonDiver7.getApplication().getGameManager().loadGameHookG1(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration2(formatVersion)) {
	    DungeonDiver7.getApplication().getGameManager().loadGameHookG2(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration3(formatVersion)) {
	    DungeonDiver7.getApplication().getGameManager().loadGameHookG3(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration4(formatVersion)) {
	    DungeonDiver7.getApplication().getGameManager().loadGameHookG4(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration5(formatVersion)) {
	    DungeonDiver7.getApplication().getGameManager().loadGameHookG5(reader);
	} else if (FormatConstants.isFormatVersionValidGeneration6(formatVersion)) {
	    DungeonDiver7.getApplication().getGameManager().loadGameHookG6(reader);
	}
    }

    @Override
    public void writeSuffix(final XDataWriter writer) throws IOException {
	DungeonDiver7.getApplication().getGameManager().saveGameHook(writer);
    }
}
