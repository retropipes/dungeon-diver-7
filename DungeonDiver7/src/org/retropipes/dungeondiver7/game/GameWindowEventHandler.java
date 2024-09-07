package org.retropipes.dungeondiver7.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.manager.dungeon.DungeonManager;

class GameWindowEventHandler extends WindowAdapter {
    GameWindowEventHandler() {
        // Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent we) {
        try {
    	final var app = DungeonDiver7.getStuffBag();
    	var success = false;
    	var status = 0;
    	if (app.getDungeonManager().getDirty()) {
    	    app.getDungeonManager();
    	    status = DungeonManager.showSaveDialog();
    	    if (status == CommonDialogs.YES_OPTION) {
    		app.getDungeonManager();
    		success = DungeonManager.saveGame();
    		if (success) {
    		    app.getGame().exitGame();
    		}
    	    } else if (status == CommonDialogs.NO_OPTION) {
    		app.getGame().exitGame();
    	    }
    	} else {
    	    app.getGame().exitGame();
    	}
        } catch (final Exception ex) {
    	DungeonDiver7.logError(ex);
        }
    }
}