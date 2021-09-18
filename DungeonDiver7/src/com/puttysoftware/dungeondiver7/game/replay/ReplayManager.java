/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game.replay;

import java.awt.FileDialog;
import java.io.File;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utilities.Extension;
import com.puttysoftware.fileutils.FilenameChecker;

public class ReplayManager {
	// Constructors
	private ReplayManager() {
		// Do nothing
	}

	// Methods
	public static void loadLPB() {
		final Application app = DungeonDiver7.getApplication();
		String filename, extension, file, dir;
		final String lastOpen = PrefsManager.getLastDirOpen();
		final FileDialog fd = new FileDialog(app.getOutputFrame(),
				LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_LOAD_PLAYBACK),
				FileDialog.LOAD);
		fd.setDirectory(lastOpen);
		fd.setVisible(true);
		file = fd.getFile();
		dir = fd.getDirectory();
		if (file != null && dir != null) {
			filename = dir + file;
			extension = ReplayManager.getExtension(filename);
			if (extension.equals(Extension.getOldPlaybackExtension())) {
				ReplayManager.loadFile(filename);
			} else {
				CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
						LocaleConstants.DIALOG_STRING_NON_PLAYBACK_FILE));
			}
		}
	}

	public static void loadFile(final String filename) {
		if (!FilenameChecker.isFilenameOK(ReplayManager.getNameWithoutExtension(ReplayManager.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog(
					LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
							LocaleConstants.DIALOG_STRING_ILLEGAL_CHARACTERS),
					LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_LOAD));
		} else {
			final ReplayFileLoadTask lpblt = new ReplayFileLoadTask(filename);
			lpblt.start();
		}
	}

	private static String getExtension(final String s) {
		String ext = null;
		final int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	private static String getNameWithoutExtension(final String s) {
		String ext = null;
		final int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		} else {
			ext = s;
		}
		return ext;
	}

	private static String getFileNameOnly(final String s) {
		String fno = null;
		final int i = s.lastIndexOf(File.separatorChar);
		if (i > 0 && i < s.length() - 1) {
			fno = s.substring(i + 1);
		} else {
			fno = s;
		}
		return fno;
	}
}
