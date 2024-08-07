/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.current.CurrentDungeonData;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

class AnimationTask extends Thread {
    // Fields
    private boolean stop = false;

    // Constructors
    public AnimationTask() {
	this.setName(Strings.untranslated(Untranslated.ANIMATOR_NAME));
	this.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
	try {
	    final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	    while (!this.stop) {
		final var pz = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
		final var maxX = a.getRows();
		final var maxY = a.getColumns();
		final var maxW = DungeonConstants.NUM_LAYERS;
		for (var x = 0; x < maxX; x++) {
		    for (var y = 0; y < maxY; y++) {
			for (var w = 0; w < maxW; w++) {
			    synchronized (CurrentDungeonData.LOCK_OBJECT) {
				final var oldFN = a.getCell(x, y, pz, w).getFrameNumber();
				a.getCell(x, y, pz, w).toggleFrameNumber();
				final var newFN = a.getCell(x, y, pz, w).getFrameNumber();
				if (oldFN != newFN) {
				    a.markAsDirty(x, y, pz);
				}
			    }
			}
		    }
		}
		DungeonDiver7.getStuffBag().getGameLogic().redrawDungeon();
		try {
		    Thread.sleep(200);
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }

    void stopAnimator() {
	this.stop = true;
    }
}
