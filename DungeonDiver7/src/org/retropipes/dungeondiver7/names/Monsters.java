/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.names;

import org.retropipes.dungeondiver7.locale.Strings;

public class Monsters {
    public static final String getImageFilename(final int ID) {
	final var tempMonID = Integer.toString(ID);
	String monID;
	if (tempMonID.length() == 1) {
	    monID = "0" + tempMonID;
	} else {
	    monID = tempMonID;
	}
	return monID;
    }

    public static final String getType(final int zoneID, final int ID) {
	return Strings.monsterzone(zoneID, ID);
    }

    // Private constructor
    private Monsters() {
	// Do nothing
    }
}