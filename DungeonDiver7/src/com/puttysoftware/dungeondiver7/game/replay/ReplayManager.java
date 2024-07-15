/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game.replay;

import java.io.File;

import org.retropipes.diane.fileio.utility.FilenameChecker;
import org.retropipes.diane.gui.dialog.CommonDialogs;

import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.prefs.Prefs;

public class ReplayManager {
	private static String getExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	private static String getFileNameOnly(final String s) {
		String fno = null;
		final var i = s.lastIndexOf(File.separatorChar);
		if (i > 0 && i < s.length() - 1) {
			fno = s.substring(i + 1);
		} else {
			fno = s;
		}
		return fno;
	}

	private static String getNameWithoutExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		} else {
			ext = s;
		}
		return ext;
	}

	public static void loadFile(final String filename) {
		if (!FilenameChecker
				.isFilenameOK(ReplayManager.getNameWithoutExtension(ReplayManager.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog(Strings.dialog(DialogString.ILLEGAL_CHARACTERS),
					Strings.dialog(DialogString.LOAD));
		} else {
			final var lpblt = new ReplayFileLoadTask(filename);
			lpblt.start();
		}
	}

	public static void loadReplay() {
		String filename, extension;
		final var lastOpen = Prefs.getLastDirOpen();
		File file = CommonDialogs.showFileOpenDialog(new File(lastOpen), null, Strings.dialog(DialogString.LOAD));
		if (file != null) {
			filename = file.getAbsolutePath();
			extension = ReplayManager.getExtension(filename);
			if (extension.equals(Strings.fileExtension(FileExtension.REPLAY))) {
				ReplayManager.loadFile(filename);
			} else {
				CommonDialogs.showDialog(Strings.dialog(DialogString.NON_PLAYBACK_FILE));
			}
		}
	}

	// Constructors
	private ReplayManager() {
		// Do nothing
	}
}
