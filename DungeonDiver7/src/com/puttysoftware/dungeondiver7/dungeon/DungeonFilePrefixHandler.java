/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.FormatConstants;
import com.puttysoftware.fileio.XDataReader;
import com.puttysoftware.fileio.XDataWriter;

public class DungeonFilePrefixHandler implements AbstractPrefixIO {
    private static final byte FORMAT_VERSION = (byte) FormatConstants.ARENA_FORMAT_LATEST;

    @Override
    public int readPrefix(final XDataReader reader) throws IOException {
	final byte formatVer = DungeonFilePrefixHandler.readFormatVersion(reader);
	final boolean res = DungeonFilePrefixHandler.checkFormatVersion(formatVer);
	if (!res) {
	    throw new IOException(LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
		    LocaleConstants.ERROR_STRING_UNKNOWN_ARENA_FORMAT));
	}
	return formatVer;
    }

    @Override
    public void writePrefix(final XDataWriter writer) throws IOException {
	DungeonFilePrefixHandler.writeFormatVersion(writer);
    }

    private static byte readFormatVersion(final XDataReader reader) throws IOException {
	return reader.readByte();
    }

    private static boolean checkFormatVersion(final byte version) {
	if (version > DungeonFilePrefixHandler.FORMAT_VERSION) {
	    return false;
	} else {
	    return true;
	}
    }

    private static void writeFormatVersion(final XDataWriter writer) throws IOException {
	writer.writeByte(DungeonFilePrefixHandler.FORMAT_VERSION);
    }
}
