/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.names;

import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.locale.LocalizedFile;

public class BossNames {
    // Private constructor
    private BossNames() {
	// Do nothing
    }

    public static final String getName(final int ID) {
	final String tempBossID = Integer.toString(ID);
	String bossID;
	if (tempBossID.length() == 1) {
	    bossID = "0" + tempBossID;
	} else {
	    bossID = tempBossID;
	}
	return LocaleLoader.loadString(LocalizedFile.BOSSES, bossID);
    }
}