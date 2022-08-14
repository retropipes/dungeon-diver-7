/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.IOException;

import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public interface AbstractPrefixIO {
    void writePrefix(FileIOWriter writer) throws IOException;

    int readPrefix(FileIOReader reader) throws IOException;
}
