/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.ai.window;

import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.creature.Creature;

class CommonWindowAIParts {
    // Constants
    static final int SPELL_INDEX_HEAL = 1;

    static boolean check(final int effChance) {
	final var random = new RandomRange(1, 100);
	final var chance = random.generate();
	if (chance <= effChance) {
	    return true;
	} else {
	    // Not acting
	    return false;
	}
    }

    static int getMaxCastIndex(final Creature c) {
	final var currMP = c.getCurrentMP();
	final var allCosts = c.getSpellBook().getAllSpellCosts();
	var result = -1;
	if (currMP > 0) {
	    for (var x = 0; x < allCosts.length; x++) {
		if (currMP >= allCosts[x]) {
		    result = x;
		}
	    }
	}
	return result;
    }

    // Constructor
    private CommonWindowAIParts() {
	// Do nothing
    }
}
