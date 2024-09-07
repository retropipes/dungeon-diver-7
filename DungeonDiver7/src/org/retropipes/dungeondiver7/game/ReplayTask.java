/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.settings.Settings;

class ReplayTask extends Thread {
    // Constructors
    public ReplayTask() {
	// Do nothing
    }

    @Override
    public void run() {
	final var gm = DungeonDiver7.getStuffBag().getGame();
	var result = true;
	while (result) {
	    result = gm.replayLastMove();
	    // Delay, for animation purposes
	    try {
		Thread.sleep(Settings.getReplaySpeed());
	    } catch (final InterruptedException ie) {
		// Ignore
	    }
	}
	gm.replayDone();
    }
}
