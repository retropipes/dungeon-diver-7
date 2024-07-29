/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.loader.extmusic;

import java.io.File;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.FileUtilities;

public class ExternalMusicSaveTask extends Thread {
    // Fields
    private final String filename;
    private final String pathname;
    private final String basePath;

    // Constructors
    public ExternalMusicSaveTask(final String base, final String path, final String file) {
	this.basePath = base;
	this.filename = file;
	this.pathname = path;
	this.setName("External Music Writer");
    }

    @Override
    public void run() {
	try {
	    FileUtilities.copyFile(new File(this.pathname + this.filename),
		    new File(this.basePath + File.separator + this.filename.toLowerCase()));
	} catch (final Exception ex) {
	    Diane.handleError(ex);
	}
    }
}
