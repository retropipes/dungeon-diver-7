/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.map.time;

import java.util.TimerTask;

import org.retropipes.diane.Diane;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

class MapTimeBattlePlayerTask extends TimerTask {
    private final MapTimeBattleLogic logic;

    public MapTimeBattlePlayerTask(MapTimeBattleLogic mapTimeBattleLogic) {
	this.logic = mapTimeBattleLogic;
    }

    @Override
    public void run() {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    final var b = app.getBattle();
	    if (app.getMode() == StuffBag.STATUS_BATTLE && b instanceof MapTimeBattleLogic) {
		final var gui = this.logic.battleGUI;
		if (!gui.isPlayerActionBarFull()) {
		    gui.turnEventHandlersOff();
		    gui.updatePlayerActionBarValue();
		    if (gui.isPlayerActionBarFull()) {
			SoundLoader.playSound(Sounds.PLAYER_UP);
			gui.turnEventHandlersOn();
		    }
		} else {
		    gui.turnEventHandlersOn();
		}
	    }
	} catch (final Throwable t) {
	    Diane.handleError(t);
	}
    }
}