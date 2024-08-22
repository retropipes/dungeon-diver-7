/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.ai.window;

import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.creature.Creature;

public class HardWindowAI extends WindowAI {
    private static final int CAST_SPELL_CHANCE = 40;
    private static final int STEAL_CHANCE = 8;
    private static final int DRAIN_CHANCE = 40;
    private static final int HEAL_THRESHOLD = 40;
    private static final int FLEE_CHANCE = 5;
    // Fields
    private int[] roundsRemaining;

    // Constructors
    public HardWindowAI() {
	// Do nothing
    }

    @Override
    public BattleAction getNextAction(final Creature c) {
	if (this.roundsRemaining == null) {
	    this.roundsRemaining = new int[c.getSpellBook().getSpellCount()];
	}
	if (this.spellCheck(c)) {
	    // Cast a spell
	    return BattleAction.CAST_SPELL;
	} else if (CommonWindowAIParts.check(HardWindowAI.STEAL_CHANCE)) {
	    // Steal
	    return BattleAction.STEAL;
	} else if (CommonWindowAIParts.check(HardWindowAI.DRAIN_CHANCE)) {
	    // Drain MP
	    return BattleAction.DRAIN;
	} else if (CommonWindowAIParts.check(HardWindowAI.FLEE_CHANCE)) {
	    // Flee
	    return BattleAction.FLEE;
	} else {
	    // Something hostile is nearby, so attack it
	    return BattleAction.ATTACK;
	}
    }

    @Override
    public void newRoundHook() {
	// Decrement effect counters
	for (var z = 0; z < this.roundsRemaining.length; z++) {
	    if (this.roundsRemaining[z] > 0) {
		this.roundsRemaining[z]--;
	    }
	}
    }

    private boolean spellCheck(final Creature c) {
	final var random = new RandomRange(1, 100);
	final var chance = random.generate();
	if (chance <= HardWindowAI.CAST_SPELL_CHANCE) {
	    final var maxIndex = CommonWindowAIParts.getMaxCastIndex(c);
	    if (maxIndex > -1) {
		// Select a random spell to cast
		final var randomSpell = new RandomRange(0, maxIndex);
		final var randomSpellID = randomSpell.generate();
		// Healing spell was selected - is healing needed?
		if ((randomSpellID == CommonWindowAIParts.SPELL_INDEX_HEAL)
			&& (c.getCurrentHP() > c.getMaximumHP() * HardWindowAI.HEAL_THRESHOLD / 100)) {
		    // Do not need healing
		    return false;
		}
		if (this.roundsRemaining[randomSpellID] == 0) {
		    this.spell = c.getSpellBook().getSpellByID(randomSpellID);
		    this.roundsRemaining[randomSpellID] = this.spell.getEffect().getInitialRounds();
		    return true;
		} else {
		    // Spell selected already active
		    return false;
		}
	    } else {
		// Not enough MP to cast anything
		return false;
	    }
	} else {
	    // Not casting a spell
	    return false;
	}
    }
}