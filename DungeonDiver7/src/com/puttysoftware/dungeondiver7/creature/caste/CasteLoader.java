/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.caste;

import com.puttysoftware.dungeondiver7.creature.caste.predefined.Annihilator;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.Buffer;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.Curer;
import com.puttysoftware.dungeondiver7.creature.caste.predefined.Debuffer;

class CasteLoader {
	// Constructors
	private CasteLoader() {
		// Do nothing
	}

	static Caste loadCaste(final String name) {
		if (name.equals(CasteConstants.CASTE_NAMES[CasteConstants.CASTE_ANNIHILATOR])) {
			return new Annihilator();
		}
		if (name.equals(CasteConstants.CASTE_NAMES[CasteConstants.CASTE_BUFFER])) {
			return new Buffer();
		}
		if (name.equals(CasteConstants.CASTE_NAMES[CasteConstants.CASTE_CURER])) {
			return new Curer();
		}
		if (name.equals(CasteConstants.CASTE_NAMES[CasteConstants.CASTE_DEBUFFER])) {
			return new Debuffer();
		}
		// Invalid caste name
		return null;
	}
}
