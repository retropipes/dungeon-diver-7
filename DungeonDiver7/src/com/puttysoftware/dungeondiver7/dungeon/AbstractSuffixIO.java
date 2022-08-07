/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.io.IOException;

import com.puttysoftware.fileio.XDataReader;
import com.puttysoftware.fileio.XDataWriter;

public interface AbstractSuffixIO {
    void writeSuffix(XDataWriter writer) throws IOException;

    void readSuffix(XDataReader reader, int formatVersion) throws IOException;
}
