/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game.replay;

import java.io.FileInputStream;
import java.io.IOException;

class ReplayFileLoader {
    // Fields
    private static byte[] lname;
    private static byte[] author;
    private static byte[] lnum;
    private static byte[] rawsize;
    private static int size;
    private static byte[] data;
    private static final int LNAME_SIZE = 31;
    private static final int AUTHOR_SIZE = 31;
    private static final int LNUM_SIZE = 2;
    private static final int RAWSIZE_SIZE = 2;

    private ReplayFileLoader() {
	// Do nothing
    }

    static boolean loadLPB(final FileInputStream file) {
	try {
	    ReplayFileLoader.lname = new byte[ReplayFileLoader.LNAME_SIZE];
	    ReplayFileLoader.author = new byte[ReplayFileLoader.AUTHOR_SIZE];
	    ReplayFileLoader.lnum = new byte[ReplayFileLoader.LNUM_SIZE];
	    ReplayFileLoader.rawsize = new byte[ReplayFileLoader.RAWSIZE_SIZE];
	    int bytesread = file.read(ReplayFileLoader.lname, 0, ReplayFileLoader.LNAME_SIZE);
	    if (bytesread != ReplayFileLoader.LNAME_SIZE) {
		return false;
	    }
	    bytesread = file.read(ReplayFileLoader.author, 0, ReplayFileLoader.AUTHOR_SIZE);
	    if (bytesread != ReplayFileLoader.AUTHOR_SIZE) {
		return false;
	    }
	    bytesread = file.read(ReplayFileLoader.lnum, 0, ReplayFileLoader.LNUM_SIZE);
	    if (bytesread != ReplayFileLoader.LNUM_SIZE) {
		return false;
	    }
	    bytesread = file.read(ReplayFileLoader.rawsize, 0, ReplayFileLoader.RAWSIZE_SIZE);
	    if (bytesread != ReplayFileLoader.RAWSIZE_SIZE) {
		return false;
	    }
	    ReplayFileLoader.size = ReplayFileLoader.toInt(ReplayFileLoader.rawsize);
	    ReplayFileLoader.data = new byte[ReplayFileLoader.size];
	    bytesread = file.read(ReplayFileLoader.data, 0, ReplayFileLoader.size);
	    if (bytesread != ReplayFileLoader.size) {
		return false;
	    }
	    return true;
	} catch (final IOException ioe) {
	    return false;
	}
    }

    static byte[] getData() {
	return ReplayFileLoader.data;
    }

    private static int toInt(final byte[] d) {
	if (d == null || d.length != 2) {
	    return 0x0;
	}
	return 0xff & d[0] | (0xff & d[1]) << 8;
    }
}