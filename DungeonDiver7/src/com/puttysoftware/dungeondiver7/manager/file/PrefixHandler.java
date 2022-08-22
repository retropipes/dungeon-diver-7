/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.utility.FileFormats;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public class PrefixHandler implements AbstractPrefixIO {
    private static final byte FORMAT_VERSION = (byte) FileFormats.DUNGEON_LATEST;

    @Override
    public int readPrefix(final FileIOReader reader) throws IOException {
	final var formatVer = PrefixHandler.readFormatVersion(reader);
	final var res = PrefixHandler.checkFormatVersion(formatVer);
	if (!res) {
	    throw new IOException("Unsupported maze format version: " + formatVer);
	}
	return formatVer;
    }

    @Override
    public void writePrefix(final FileIOWriter writer) throws IOException {
	PrefixHandler.writeFormatVersion(writer);
    }

    private static byte readFormatVersion(final FileIOReader reader) throws IOException {
	return reader.readByte();
    }

    private static boolean checkFormatVersion(final byte version) {
	return version <= PrefixHandler.FORMAT_VERSION;
    }

    private static void writeFormatVersion(final FileIOWriter writer) throws IOException {
	writer.writeByte(PrefixHandler.FORMAT_VERSION);
    }
}
