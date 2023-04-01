/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.spell;

import java.util.Objects;

import com.puttysoftware.dungeondiver7.battle.BattleTarget;
import com.puttysoftware.dungeondiver7.effect.Effect;
import com.puttysoftware.dungeondiver7.loader.Sounds;

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

    public Effect getEffect() {
	return this.effect;
    }

    public int getCost() {
	return this.cost;
    }

    BattleTarget getTarget() {
	return this.target;
    }

    Sounds getSound() {
	return this.soundEffect;
    }

    @Override
    public int hashCode() {
	return Objects.hash(this.cost, this.effect, this.soundEffect, this.target);
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
}
