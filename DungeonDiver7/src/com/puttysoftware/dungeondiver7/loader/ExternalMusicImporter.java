/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.fileio.utility.FileUtilities;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.utility.IDGenerator;

public class ExternalMusicImporter {
    private static final String MAC_PREFIX = "HOME";
    private static final String WIN_PREFIX = "APPDATA";
    private static final String UNIX_PREFIX = "HOME";
    private static final String MAC_SOUND_DIR = "/Library/Application Support/Putty Software/DungeonDiver7/TempMusic";
    private static final String WIN_SOUND_DIR = "\\Putty Software\\DungeonDiver7\\TempMusic";
    private static final String UNIX_SOUND_DIR = "/.puttysoftware/dungeondiver7/tempmusic";
    private static File destFile;

    public static File getDestinationFile() {
	return ExternalMusicImporter.destFile;
    }

    private static String getDirPrefix() {
	final var osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return System.getenv(ExternalMusicImporter.MAC_PREFIX);
	}
	if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return System.getenv(ExternalMusicImporter.WIN_PREFIX);
	}
	// Other - assume UNIX-like
	return System.getenv(ExternalMusicImporter.UNIX_PREFIX);
    }

    private static String getExtension(final File f) {
	String ext = null;
	final var s = f.getName();
	final var i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i).toLowerCase();
	}
	return ext;
    }

    public static String getMusicBasePath() {
	final var b = new StringBuilder();
	b.append(ExternalMusicImporter.getDirPrefix());
	b.append(ExternalMusicImporter.getMusicDirectory());
	return b.toString();
    }

    private static String getMusicDirectory() {
	final var osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return ExternalMusicImporter.MAC_SOUND_DIR;
	}
	if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return ExternalMusicImporter.WIN_SOUND_DIR;
	}
	// Other - assume UNIX-like
	return ExternalMusicImporter.UNIX_SOUND_DIR;
    }

    public static void importMusic(final File source) {
	final var basePath = ExternalMusicImporter.getMusicBasePath();
	final var musicfilename = IDGenerator.generateRandomFilename() + ExternalMusicImporter.getExtension(source);
	final var dest = new File(basePath + File.separator + musicfilename);
	ExternalMusicImporter.destFile = dest;
	try {
	    if (!dest.getParentFile().exists()) {
		dest.getParentFile().mkdirs();
	    }
	    FileUtilities.copyFile(source, dest);
	    MusicLoader.deleteExternalMusicFile();
	    DungeonDiver7.getStuffBag().getEditor().setMusicFilename(musicfilename);
	    CommonDialogs.showDialog("Music successfully imported.");
	} catch (final IOException io) {
	    // Ignore
	}
    }
}
