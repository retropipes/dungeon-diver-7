/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.names;

public class Zones {
	// Private constructor
	private Zones() {
		// Do nothing
	}

	public static final String getZoneNumber(final int ID) {
		final var tempZoneID = Integer.toString(ID);
		String zoneID;
		if (tempZoneID.length() == 1) {
			zoneID = "0" + tempZoneID;
		} else {
			zoneID = tempZoneID;
		}
		return zoneID;
	}
}