/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.File;

import org.retropipes.diane.fileio.utility.FileUtilities;

import com.puttysoftware.dungeondiver7.DungeonDiver7;

public class ExternalMusicSaveTask extends Thread {
	// Fields
	private final String filename;
	private final String pathname;

	// Constructors
	public ExternalMusicSaveTask(final String path, final String file) {
		this.filename = file;
		this.pathname = path;
		this.setName("External Music Writer");
	}

	@Override
	public void run() {
		try {
			final var basePath = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
					.getDungeonTempMusicFolder();
			FileUtilities.copyFile(new File(this.pathname + this.filename),
					new File(basePath + File.separator + this.filename.toLowerCase()));
		} catch (final Exception ex) {
			DungeonDiver7.logError(ex);
		}
	}
}
