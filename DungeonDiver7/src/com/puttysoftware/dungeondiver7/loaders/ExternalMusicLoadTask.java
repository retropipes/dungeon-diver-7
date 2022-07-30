/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loaders;

import java.io.File;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.editor.ExternalMusic;

public class ExternalMusicLoadTask extends Thread {
    // Fields
    private ExternalMusic gameExternalMusic;
    private final String filename;

    // Constructors
    public ExternalMusicLoadTask(final String file) {
	this.filename = file;
	this.setName("External Music Loader");
    }

    // Methods
    @Override
    public void run() {
	final Application app = DungeonDiver7.getApplication();
	final AbstractDungeon a = app.getDungeonManager().getDungeon();
	try {
	    this.gameExternalMusic = new ExternalMusic();
	    this.gameExternalMusic.setName(ExternalMusicLoadTask.getFileNameOnly(this.filename));
	    this.gameExternalMusic.setPath(a.getDungeonTempMusicFolder());
	    MusicLoader.setExternalMusic(this.gameExternalMusic);
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	}
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
