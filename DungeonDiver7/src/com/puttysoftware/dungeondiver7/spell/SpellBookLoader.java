/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.spell;

import com.puttysoftware.dungeondiver7.creature.job.JobConstants;
import com.puttysoftware.dungeondiver7.creature.job.predefined.AnnihilatorSpellBook;
import com.puttysoftware.dungeondiver7.creature.job.predefined.BufferSpellBook;
import com.puttysoftware.dungeondiver7.creature.job.predefined.CurerSpellBook;
import com.puttysoftware.dungeondiver7.creature.job.predefined.DebufferSpellBook;

public class SpellBookLoader {
    public static SpellBook loadSpellBook(final int sbid) {
	return switch (sbid) {
	case JobConstants.JOB_ANNIHILATOR -> new AnnihilatorSpellBook();
	case JobConstants.JOB_BUFFER -> new BufferSpellBook();
	case JobConstants.JOB_CURER -> new CurerSpellBook();
	case JobConstants.JOB_DEBUFFER -> new DebufferSpellBook();
	default -> /* Invalid caste name */ null;
	};
    }

    // Constructors
    private SpellBookLoader() {
	// Do nothing
    }
}
