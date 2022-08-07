/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import java.io.File;

import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.fileutils.DirectoryUtilities;

public class CleanupTask {
    private CleanupTask() {
	// Do nothing
    }

    public static void cleanUp() {
	try {
	    final File dirToDelete = new File(AbstractDungeon.getDungeonTempFolder());
	    DirectoryUtilities.removeDirectory(dirToDelete);
	} catch (final Throwable t) {
	    // Ignore
	}
    }
}
