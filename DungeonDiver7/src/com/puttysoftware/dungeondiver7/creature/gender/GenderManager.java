/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.gender;

import com.puttysoftware.diane.gui.CommonDialogs;

public class GenderManager {
    private static boolean CACHE_CREATED = false;
    private static Gender[] CACHE;

    public static Gender selectGender() {
	final var names = GenderConstants.GENDER_NAMES;
	final var dialogResult = CommonDialogs.showInputDialog("Select a Gender", "Create Character", names, names[0]);
	if (dialogResult == null) {
	    return null;
	}
	int index;
	for (index = 0; index < names.length; index++) {
	    if (dialogResult.equals(names[index])) {
		break;
	    }
	}
	return GenderManager.getGender(index);
    }

    public static Gender getGender(final int genderID) {
	if (!GenderManager.CACHE_CREATED) {
	    // Create cache
	    GenderManager.CACHE = new Gender[GenderConstants.GENDERS_COUNT];
	    for (var x = 0; x < GenderConstants.GENDERS_COUNT; x++) {
		GenderManager.CACHE[x] = new Gender(x);
	    }
	    GenderManager.CACHE_CREATED = true;
	}
	return GenderManager.CACHE[genderID];
    }
}
