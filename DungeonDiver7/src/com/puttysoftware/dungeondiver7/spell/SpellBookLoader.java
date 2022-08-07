/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.spell;

import com.puttysoftware.dungeondiver7.creature.caste.CasteConstants;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.AnnihilatorSpellBook;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.BufferSpellBook;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.CurerSpellBook;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.DebufferSpellBook;

public class SpellBookLoader {
    // Constructors
    private SpellBookLoader() {
	// Do nothing
    }

    // Methods
    public static SpellBook loadSpellBook(final int sbid) {
	if (sbid == CasteConstants.CASTE_ANNIHILATOR) {
	    return new AnnihilatorSpellBook();
	} else if (sbid == CasteConstants.CASTE_BUFFER) {
	    return new BufferSpellBook();
	} else if (sbid == CasteConstants.CASTE_CURER) {
	    return new CurerSpellBook();
	} else if (sbid == CasteConstants.CASTE_DEBUFFER) {
	    return new DebufferSpellBook();
	} else {
	    // Invalid caste name
	    return null;
	}
    }
}
