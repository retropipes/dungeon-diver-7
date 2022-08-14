/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.ltv4;

import java.io.FileInputStream;

import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeonData;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.InvalidDungeonException;

class LaserTankV4File {
    private LaserTankV4File() {
	// Do nothing
    }

    static void loadOldFile(final AbstractDungeon a, final FileInputStream file) throws InvalidDungeonException {
	CurrentDungeonData t = null;
	int levelCount = 0;
	do {
	    a.switchLevel(levelCount);
	    t = LaserTankV4FileLevel.loadAndConvert(file, a);
	    if (t != null) {
		levelCount++;
		a.setData(t, levelCount);
		final int[] found = a.findPlayer(1);
		if (found == null) {
		    throw new InvalidDungeonException(LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
			    LocaleConstants.ERROR_STRING_TANK_LOCATION));
		} else {
		    a.setStartColumn(0, found[0]);
		    a.setStartRow(0, found[1]);
		    a.setStartFloor(0, found[2]);
		}
		a.save();
		a.switchLevel(levelCount);
	    }
	} while (t != null);
    }
}
