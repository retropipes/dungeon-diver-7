/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game.replay;

import java.io.FileInputStream;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.ErrorString;
import org.retropipes.dungeondiver7.locale.GameString;
import org.retropipes.dungeondiver7.locale.Strings;

class ReplayFile {
    private static void decodeData(final byte d) {
	final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	switch (d) {
	case 0x20:
	    gm.loadReplay(true, 0, 0);
	    break;
	case 0x25:
	    gm.loadReplay(false, -1, 0);
	    break;
	case 0x26:
	    gm.loadReplay(false, 0, -1);
	    break;
	case 0x27:
	    gm.loadReplay(false, 1, 0);
	    break;
	case 0x28:
	    gm.loadReplay(false, 0, 1);
	    break;
	default:
	    break;
	}
    }

    static void loadLPB(final FileInputStream file) {
	// Load LPB
	final var success = ReplayFileLoader.loadLPB(file);
	if (!success) {
	    CommonDialogs.showErrorDialog(Strings.error(ErrorString.REPLAY_LOAD_FAILURE),
		    Strings.game(GameString.LOAD_PLAYBACK));
	} else {
	    final var gm = DungeonDiver7.getStuffBag().getGameLogic();
	    gm.clearReplay();
	    final var data = ReplayFileLoader.getData();
	    for (var x = data.length - 1; x >= 0; x--) {
		ReplayFile.decodeData(data[x]);
	    }
	    CommonDialogs.showTitledDialog(Strings.game(GameString.PLAYBACK_LOADED),
		    Strings.game(GameString.LOAD_PLAYBACK));
	}
    }

    private ReplayFile() {
	// Do nothing
    }
}