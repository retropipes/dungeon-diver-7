/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.names;

import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.LocalizedFile;

public class MonsterNames {
    // Private constructor
    private MonsterNames() {
	// Do nothing
    }

    public static final String getName(final int ID) {
	final String tempMonID = Integer.toString(ID);
	String monID;
	if (tempMonID.length() == 1) {
	    monID = "0" + tempMonID;
	} else {
	    monID = tempMonID;
	}
	return monID;
    }

    public static final String getType(final int zoneID, final int ID) {
	final String tempMonID = Integer.toString(ID);
	String monID;
	if (tempMonID.length() == 1) {
	    monID = "0" + tempMonID;
	} else {
	    monID = tempMonID;
	}
	return ZoneNames.getName(zoneID) + " " + LocaleLoader.loadString(LocalizedFile.MONSTERS, monID);
    }
}