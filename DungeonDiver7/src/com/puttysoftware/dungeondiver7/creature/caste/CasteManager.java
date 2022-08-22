/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.caste;

import javax.swing.JFrame;

import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.spell.SpellBook;
import com.puttysoftware.dungeondiver7.spell.SpellBookLoader;

public class CasteManager {
    private static boolean CACHE_CREATED = false;
    private static Caste[] CACHE;
    private static String[] DESC_CACHE;

    public static Caste selectCaste(final JFrame owner) {
	CasteManager.createCache();
	final var names = CasteConstants.CASTE_NAMES;
	final var dialogResult = PartyManager.showCreationDialog(owner, "Select a Caste", "Create Character", names,
		CasteManager.DESC_CACHE);
	if (dialogResult == null) {
	    return null;
	}
	int index;
	for (index = 0; index < names.length; index++) {
	    if (dialogResult.equals(names[index])) {
		break;
	    }
	}
	return CasteManager.getCaste(index);
    }

    public static Caste getCaste(final int casteID) {
	CasteManager.createCache();
	return CasteManager.CACHE[casteID];
    }

    public static SpellBook getSpellBookByID(final int ID) {
	return SpellBookLoader.loadSpellBook(ID);
    }

    private static void createCache() {
	if (!CasteManager.CACHE_CREATED) {
	    // Create cache
	    CasteManager.CACHE = new Caste[CasteConstants.CASTES_COUNT];
	    CasteManager.DESC_CACHE = new String[CasteConstants.CASTES_COUNT];
	    for (var x = 0; x < CasteConstants.CASTES_COUNT; x++) {
		CasteManager.CACHE[x] = CasteLoader.loadCaste(Caste.casteIDtoName(x));
		CasteManager.DESC_CACHE[x] = CasteManager.CACHE[x].getDescription();
	    }
	    CasteManager.CACHE_CREATED = true;
	}
    }
}
