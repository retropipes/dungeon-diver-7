/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.IOException;

import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;

public interface AbstractPrefixIO {
    int readPrefix(DataIOReader reader) throws IOException;

    void writePrefix(DataIOWriter writer) throws IOException;
}
