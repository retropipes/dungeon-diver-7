/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.prefs.Prefs;

class ReplayTask extends Thread {
    // Constructors
    public ReplayTask() {
	// Do nothing
    }

    @Override
    public void run() {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	var result = true;
	while (result) {
	    result = gm.replayLastMove();
	    // Delay, for animation purposes
	    try {
		Thread.sleep(Prefs.getReplaySpeed());
	    } catch (final InterruptedException ie) {
		// Ignore
	    }
	}
	gm.replayDone();
    }
}
