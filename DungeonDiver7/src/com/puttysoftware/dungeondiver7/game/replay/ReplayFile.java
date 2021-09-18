/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game.replay;

import java.io.FileInputStream;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.game.GameManager;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;

class ReplayFile {
	private ReplayFile() {
		// Do nothing
	}

	static void loadLPB(final FileInputStream file) {
		// Load LPB
		final boolean success = ReplayFileLoader.loadLPB(file);
		if (!success) {
			CommonDialogs.showErrorDialog(
					LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
							LocaleConstants.ERROR_STRING_LPB_LOAD_FAILURE),
					LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
							LocaleConstants.GAME_STRING_LOAD_PLAYBACK));
		} else {
			final GameManager gm = DungeonDiver7.getApplication().getGameManager();
			gm.clearReplay();
			final byte[] data = ReplayFileLoader.getData();
			for (int x = data.length - 1; x >= 0; x--) {
				ReplayFile.decodeData(data[x]);
			}
			CommonDialogs.showTitledDialog(
					LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
							LocaleConstants.GAME_STRING_PLAYBACK_LOADED),
					LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
							LocaleConstants.GAME_STRING_LOAD_PLAYBACK));
		}
	}

	private static void decodeData(final byte d) {
		final GameManager gm = DungeonDiver7.getApplication().getGameManager();
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
}