/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.caste;

import com.puttysoftware.dungeondiver7.loader.CasteDescriptionManager;

public class Caste {
    static String casteIDtoName(final int casteID) {
	return CasteConstants.CASTE_NAMES[casteID];
    }

    private final int casteID;
    private final String desc;

    public Caste(final int cid) {
	this.desc = CasteDescriptionManager.getCasteDescription(cid);
	this.casteID = cid;
    }

    public final int getCasteID() {
	return this.casteID;
    }

    public String getDescription() {
	return this.desc;
    }
}
