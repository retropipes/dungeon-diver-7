/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeonData;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

class AnimationTask extends Thread {
    // Fields
    private boolean stop = false;

    // Constructors
    public AnimationTask() {
	this.setName(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_ANIMATOR_NAME));
	this.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
	try {
	    final AbstractDungeon a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	    while (!this.stop) {
		final int pz = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
		final int maxX = a.getRows();
		final int maxY = a.getColumns();
		final int maxW = DungeonConstants.NUM_LAYERS;
		for (int x = 0; x < maxX; x++) {
		    for (int y = 0; y < maxY; y++) {
			for (int w = 0; w < maxW; w++) {
			    synchronized (CurrentDungeonData.LOCK_OBJECT) {
				final int oldFN = a.getCell(x, y, pz, w).getFrameNumber();
				a.getCell(x, y, pz, w).toggleFrameNumber();
				final int newFN = a.getCell(x, y, pz, w).getFrameNumber();
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
