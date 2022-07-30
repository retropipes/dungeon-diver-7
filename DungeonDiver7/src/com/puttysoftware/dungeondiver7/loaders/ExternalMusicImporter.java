/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loaders;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.utilities.IDGenerator;
import com.puttysoftware.fileutils.FileUtilities;

public class ExternalMusicImporter {
    private static final String MAC_PREFIX = "HOME";
    private static final String WIN_PREFIX = "APPDATA";
    private static final String UNIX_PREFIX = "HOME";
    private static final String MAC_SOUND_DIR = "/Library/Application Support/Putty Software/DungeonDiver7/TempMusic";
    private static final String WIN_SOUND_DIR = "\\Putty Software\\DungeonDiver7\\TempMusic";
    private static final String UNIX_SOUND_DIR = "/.puttysoftware/dungeondiver7/tempmusic";
    private static File destFile;

    public static void importMusic(final File source) {
	final String basePath = ExternalMusicImporter.getMusicBasePath();
	final String musicfilename = IDGenerator.generateRandomFilename() + LocaleConstants.COMMON_STRING_NOTL_PERIOD
		+ MusicLoader.getExtension(source);
	final File dest = new File(basePath + File.separator + musicfilename);
	ExternalMusicImporter.destFile = dest;
	try {
	    if (!dest.getParentFile().exists()) {
		dest.getParentFile().mkdirs();
	    }
	    FileUtilities.copyFile(source, dest);
	    MusicLoader.deleteExternalMusicFile();
	    DungeonDiver7.getApplication().getEditor().setMusicFilename(musicfilename);
	    CommonDialogs.showDialog("Music successfully imported.");
	} catch (final IOException io) {
	    // Ignore
	}
    }

    public static File getDestinationFile() {
	return ExternalMusicImporter.destFile;
    }

    private static String getDirPrefix() {
	final String osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return System.getenv(ExternalMusicImporter.MAC_PREFIX);
	} else if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return System.getenv(ExternalMusicImporter.WIN_PREFIX);
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(ExternalMusicImporter.UNIX_PREFIX);
	}
    }

    private static String getMusicDirectory() {
	final String osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return ExternalMusicImporter.MAC_SOUND_DIR;
	} else if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return ExternalMusicImporter.WIN_SOUND_DIR;
	} else {
	    // Other - assume UNIX-like
	    return ExternalMusicImporter.UNIX_SOUND_DIR;
	}
    }

    public static String getMusicBasePath() {
	final StringBuilder b = new StringBuilder();
	b.append(ExternalMusicImporter.getDirPrefix());
	b.append(ExternalMusicImporter.getMusicDirectory());
	return b.toString();
    }
}
