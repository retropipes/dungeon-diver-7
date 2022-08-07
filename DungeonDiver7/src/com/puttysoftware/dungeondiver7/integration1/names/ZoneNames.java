/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.names;

import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.LocalizedFile;

public class ZoneNames {
    // Private constructor
    private ZoneNames() {
	// Do nothing
    }

    public static final String getName(final int ID) {
	final String tempZoneID = Integer.toString(ID);
	String zoneID;
	if (tempZoneID.length() == 1) {
	    zoneID = "0" + tempZoneID;
	} else {
	    zoneID = tempZoneID;
	}
	return LocaleLoader.loadString(LocalizedFile.ZONES, zoneID);
    }

    public static final String getZoneNumber(final int ID) {
	final String tempZoneID = Integer.toString(ID);
	String zoneID;
	if (tempZoneID.length() == 1) {
	    zoneID = "0" + tempZoneID;
	} else {
	    zoneID = tempZoneID;
	}
	return zoneID;
    }

    public static final String getZoneName(final int ID) {
	return "zone" + ZoneNames.getZoneNumber(ID) + "/";
    }
}