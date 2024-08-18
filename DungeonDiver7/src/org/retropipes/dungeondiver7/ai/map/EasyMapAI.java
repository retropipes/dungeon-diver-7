/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.ai.map;

import org.retropipes.diane.random.RandomRange;

class EasyMapAI extends MapAI {
    private static final int CAST_SPELL_CHANCE = 10;
    private static final int STEAL_CHANCE = 2;
    private static final int DRAIN_CHANCE = 10;
    private static final int HEAL_THRESHOLD = 10;
    private static final int MAX_VISION = 3;
    private static final int FLEE_CHANCE = 20;
    // Fields
    private final RandomRange randMove;
    private int failedMoveAttempts;
    private int[] roundsRemaining;

    // Constructor
    public EasyMapAI() {
	this.randMove = new RandomRange(-1, 1);
	this.failedMoveAttempts = 0;
    }

    @Override
    public int getNextAction(final MapAIContext ac) {
	if (this.roundsRemaining == null) {
	    this.roundsRemaining = new int[ac.getCharacter().getTemplate().getSpellBook().getSpellCount()];
	}
	if (this.spellCheck(ac)) {
	    // Cast a spell
	    return MapAI.ACTION_CAST_SPELL;
	}
	var there = ac.isEnemyNearby();
	if (there != null) {
	    if (CommonMapAIParts.check(ac, EasyMapAI.STEAL_CHANCE)) {
		// Steal
		return MapAI.ACTION_STEAL;
	    }
	    if (CommonMapAIParts.check(ac, EasyMapAI.DRAIN_CHANCE)) {
		// Drain MP
		return MapAI.ACTION_DRAIN;
	    }
	    if (ac.getCharacter().getAttacksLeft() > 0) {
		this.moveX = there.x;
		this.moveY = there.y;
		return MapAI.ACTION_MOVE;
	    }
	    this.failedMoveAttempts = 0;
	    return MapAI.ACTION_END_TURN;
	}
	if (CommonMapAIParts.check(ac, EasyMapAI.FLEE_CHANCE)) {
	    // Flee
	    final var awayDir = ac.runAway();
	    if (awayDir == null) {
		// Wander randomly
		this.moveX = this.randMove.generate();
		this.moveY = this.randMove.generate();
		// Don't attack self
		while (this.moveX == 0 && this.moveY == 0) {
		    this.moveX = this.randMove.generate();
		    this.moveY = this.randMove.generate();
		}
	    } else {
		this.moveX = awayDir.x;
		this.moveY = awayDir.y;
	    }
	    return MapAI.ACTION_MOVE;
	}
	// Look further
	for (var x = CommonMapAIParts.MIN_VISION + 1; x <= EasyMapAI.MAX_VISION; x++) {
	    there = ac.isEnemyNearby(x, x);
	    if (there != null) {
		// Found something hostile, move towards it
		if (!this.lastResult) {
		    this.failedMoveAttempts++;
		    if (this.failedMoveAttempts >= CommonMapAIParts.STUCK_THRESHOLD) {
			// We're stuck!
			this.failedMoveAttempts = 0;
			return MapAI.ACTION_END_TURN;
		    }
		    // Last move failed, try to move around object
		    final var randTurn = new RandomRange(0, 1);
		    final var rt = randTurn.generate();
		    if (rt == 0) {
			there = CommonMapAIParts.turnRight45(this.moveX, this.moveY);
		    } else {
			there = CommonMapAIParts.turnLeft45(this.moveX, this.moveY);
		    }
		    this.moveX = there.x;
		    this.moveY = there.y;
		} else {
		    this.moveX = (int) Math.signum(there.x);
		    this.moveY = (int) Math.signum(there.y);
		}
		break;
	    }
	}
	if (ac.getCharacter().getActionsLeft() <= 0) {
	    this.failedMoveAttempts = 0;
	    return MapAI.ACTION_END_TURN;
	}
	if (there == null) {
	    // Wander randomly
	    this.moveX = this.randMove.generate();
	    this.moveY = this.randMove.generate();
	    // Don't attack self
	    while (this.moveX == 0 && this.moveY == 0) {
		this.moveX = this.randMove.generate();
		this.moveY = this.randMove.generate();
	    }
	}
	return MapAI.ACTION_MOVE;
    }

    @Override
    public void newRoundHook() {
	// Decrement effect counters
	if (this.roundsRemaining != null) {
	    for (var z = 0; z < this.roundsRemaining.length; z++) {
		if (this.roundsRemaining[z] > 0) {
		    this.roundsRemaining[z]--;
		}
	    }
	}
    }

    private boolean spellCheck(final MapAIContext ac) {
	final var random = new RandomRange(1, 100);
	final var chance = random.generate();
	if (chance > EasyMapAI.CAST_SPELL_CHANCE) {
	    // Not casting a spell
	    return false;
	}
	final var maxIndex = CommonMapAIParts.getMaxCastIndex(ac);
	if (maxIndex <= -1 || ac.getCharacter().getSpellsLeft() <= 0) {
	    // Can't cast any more spells
	    return false;
	}
	// Select a random spell to cast
	final var randomSpell = new RandomRange(0, maxIndex);
	final var randomSpellID = randomSpell.generate();
	// Healing spell was selected - is healing needed?
	if (randomSpellID == CommonMapAIParts.SPELL_INDEX_HEAL
		&& ac.getCharacter().getTemplate().getCurrentHP() > ac.getCharacter().getTemplate().getMaximumHP()
			* EasyMapAI.HEAL_THRESHOLD / 100) {
	    // Do not need healing
	    return false;
	}
	if (this.roundsRemaining[randomSpellID] == 0) {
	    this.spell = ac.getCharacter().getTemplate().getSpellBook().getSpellByID(randomSpellID);
	    this.roundsRemaining[randomSpellID] = this.spell.getEffect().getInitialRounds();
	    return true;
	}
	// Spell selected already active
	return false;
    }
}
