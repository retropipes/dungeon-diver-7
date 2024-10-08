/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.spell;

import java.util.Objects;

import org.retropipes.dungeondiver7.creature.BattleTarget;
import org.retropipes.dungeondiver7.creature.effect.Effect;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class Spell {
    // Fields
    private final Effect effect;
    private final int cost;
    private final BattleTarget target;
    private final Sounds soundEffect;

    // Constructors
    public Spell() {
	this.effect = null;
	this.cost = 0;
	this.target = null;
	this.soundEffect = Sounds._NONE;
    }

    public Spell(final Effect newEffect, final int newCost, final BattleTarget newTarget, final Sounds sfx) {
	this.effect = newEffect;
	this.cost = newCost;
	this.target = newTarget;
	this.soundEffect = sfx;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final Spell other) || this.cost != other.cost
		|| !Objects.equals(this.effect, other.effect)) {
	    return false;
	}
	if (this.soundEffect != other.soundEffect || this.target != other.target) {
	    return false;
	}
	return true;
    }

    public int getCost() {
	return this.cost;
    }

    public Effect getEffect() {
	return this.effect;
    }

    Sounds getSound() {
	return this.soundEffect;
    }

    BattleTarget getTarget() {
	return this.target;
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.cost, this.effect, this.soundEffect, this.target);
    }
}
