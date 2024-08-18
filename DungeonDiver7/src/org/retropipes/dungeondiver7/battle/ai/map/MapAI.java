/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.ai.map;

import java.util.Objects;

import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.CreatureAI;
import org.retropipes.dungeondiver7.creature.spell.Spell;

public abstract class MapAI extends CreatureAI {
    // Fields
    protected Spell spell;
    protected int moveX;
    protected int moveY;
    protected boolean lastResult;

    // Constructor
    protected MapAI() {
	this.spell = null;
	this.moveX = 0;
	this.moveY = 0;
	this.lastResult = true;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final MapAI other) || this.lastResult != other.lastResult
		|| this.moveX != other.moveX) {
	    return false;
	}
	if (this.moveY != other.moveY || !Objects.equals(this.spell, other.spell)) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.lastResult, this.moveX, this.moveY, this.spell);
    }

    public void newRoundHook() {
	// Do nothing
    }

    @Override
    public final BattleAction getNextAction(Creature c) {
	return null; // Not used
    }
}
