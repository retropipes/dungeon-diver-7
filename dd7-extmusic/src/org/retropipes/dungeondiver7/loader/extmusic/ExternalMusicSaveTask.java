/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
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
