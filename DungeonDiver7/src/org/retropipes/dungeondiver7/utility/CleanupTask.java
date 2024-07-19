/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.utility;

import java.io.File;

import org.retropipes.diane.fileio.utility.DirectoryUtilities;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;

public class CleanupTask {
	public static void cleanUp() {
		try {
			final var dirToDelete = new File(AbstractDungeon.getDungeonTempFolder());
			DirectoryUtilities.removeDirectory(dirToDelete);
		} catch (final Throwable t) {
			// Ignore
		}
	}

	private CleanupTask() {
		// Do nothing
	}
}
