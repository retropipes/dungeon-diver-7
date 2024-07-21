/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.spell;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.creature.AbstractCreature;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;

public class SpellCaster {
	// Fields
	private static boolean NO_SPELLS_FLAG = false;

	public static boolean castSpell(final Spell cast, final AbstractCreature caster) {
		if (cast == null) {
			return false;
		}
		final var casterMP = caster.getCurrentMP();
		final var cost = cast.getCost();
		if (casterMP < cost) {
			// Not enough MP
			return false;
		}
		// Cast Spell
		caster.drain(cost);
		final var b = cast.getEffect();
		// Play spell's associated sound effect, if it has one
		final var snd = cast.getSound();
		SoundLoader.playSound(snd);
		b.resetEffect();
		final var target = SpellCaster.resolveTarget(cast, caster.getTeamID());
		if (target.isEffectActive(b)) {
			target.extendEffect(b, b.getInitialRounds());
		} else {
			b.restoreEffect();
			target.applyEffect(b);
		}
		return true;
	}

	private static AbstractCreature resolveTarget(final Spell cast, final int teamID) {
		final var target = cast.getTarget();
		switch (target) {
		case SELF:
			if (teamID == AbstractCreature.TEAM_PARTY) {
				return PartyManager.getParty().getLeader();
			}
			return DungeonDiver7.getStuffBag().getBattle().getEnemy();
		case ENEMY:
			if (teamID == AbstractCreature.TEAM_PARTY) {
				return DungeonDiver7.getStuffBag().getBattle().getEnemy();
			}
			return PartyManager.getParty().getLeader();
		default:
			return null;
		}
	}

	public static boolean selectAndCastSpell(final AbstractCreature caster) {
		var result = false;
		SpellCaster.NO_SPELLS_FLAG = false;
		final var s = SpellCaster.selectSpell(caster);
		if (s != null) {
			result = SpellCaster.castSpell(s, caster);
			if (!result && !SpellCaster.NO_SPELLS_FLAG) {
				CommonDialogs.showErrorDialog("You try to cast a spell, but realize you don't have enough MP!",
						"Select Spell");
			}
		}
		return result;
	}

	private static Spell selectSpell(final AbstractCreature caster) {
		final var book = caster.getSpellBook();
		if (book == null) {
			SpellCaster.NO_SPELLS_FLAG = true;
			CommonDialogs.showErrorDialog("You try to cast a spell, but realize you don't know any!", "Select Spell");
			return null;
		}
		final var names = book.getAllSpellNames();
		final var displayNames = book.getAllSpellNamesWithCosts();
		if (names == null || displayNames == null) {
			SpellCaster.NO_SPELLS_FLAG = true;
			CommonDialogs.showErrorDialog("You try to cast a spell, but realize you don't know any!", "Select Spell");
			return null;
		}
		// Play casting spell sound
		final var dialogResult = CommonDialogs.showInputDialog("Select a Spell to Cast", "Select Spell", displayNames,
				displayNames[0]);
		if (dialogResult == null) {
			return null;
		}
		int index;
		for (index = 0; index < displayNames.length; index++) {
			if (dialogResult.equals(displayNames[index])) {
				break;
			}
		}
		return book.getSpellByName(names[index]);
	}

	// Private Constructor
	private SpellCaster() {
		// Do nothing
	}
}
