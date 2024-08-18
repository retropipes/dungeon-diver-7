/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.map;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.battle.Battle;
import org.retropipes.dungeondiver7.prefs.Prefs;

public class MapBattleAITask extends Thread {
    // Fields
    private final Battle b;

    // Constructors
    public MapBattleAITask(final Battle battle) {
	this.setName("Map AI Runner");
	this.b = battle;
    }

    public synchronized void aiRun() {
	this.notify();
    }

    public synchronized void aiWait() {
	try {
	    this.wait();
	} catch (final InterruptedException e) {
	    // Ignore
	}
    }

    @Override
    public void run() {
	try {
	    this.aiWait();
	    while (true) {
		this.b.executeNextAIAction();
		if (this.b.getLastAIActionResult()) {
		    // Delay, for animation purposes
		    try {
			final var battleSpeed = Prefs.getBattleSpeed();
			Thread.sleep(battleSpeed);
		    } catch (final InterruptedException i) {
			// Ignore
		    }
		}
	    }
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }
}
