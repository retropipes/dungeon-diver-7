/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.damage;

import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.StatConstants;

class EasyDamageEngine extends DamageEngine {
    private static final int MULTIPLIER_MIN = 8000;
    private static final int MULTIPLIER_MAX = 16000;
    private static final int MULTIPLIER_MIN_CRIT = 22000;
    private static final int MULTIPLIER_MAX_CRIT = 34000;
    private static final int FUMBLE_CHANCE = 250;
    private static final int PIERCE_CHANCE = 1500;
    private static final int CRIT_CHANCE = 1500;
    private boolean dodged = false;
    private boolean missed = false;
    private boolean crit = false;
    private boolean pierce = false;
    private boolean fumble = false;

    @Override
    public int computeDamage(final Creature enemy, final Creature acting) {
	// Compute Damage
	final var attack = acting.getEffectedAttack();
	final var defense = enemy.getEffectedStat(StatConstants.STAT_DEFENSE);
	final var power = acting.getItems().getTotalPower();
	this.didFumble();
	if (this.fumble) {
	    // Fumble!
	    return CommonDamageEngineParts.fumbleDamage(power);
	}
	this.didPierce();
	this.didCrit();
	double rawDamage;
	if (this.pierce) {
	    rawDamage = attack;
	} else {
	    rawDamage = attack - defense;
	}
	final var rHit = CommonDamageEngineParts.chance();
	var aHit = acting.getHit();
	if (this.crit || this.pierce) {
	    // Critical hits and piercing hits
	    // always connect
	    aHit = CommonDamageEngineParts.ALWAYS;
	}
	if (rHit > aHit) {
	    // Weapon missed
	    this.missed = true;
	    this.dodged = false;
	    this.crit = false;
	    return 0;
	}
	final var rEvade = CommonDamageEngineParts.chance();
	final var aEvade = enemy.getEvade();
	if (rEvade < aEvade) {
	    // Enemy dodged
	    this.missed = false;
	    this.dodged = true;
	    this.crit = false;
	    return 0;
	}
	// Hit
	this.missed = false;
	this.dodged = false;
	RandomRange rDamage;
	if (this.crit) {
	    rDamage = new RandomRange(EasyDamageEngine.MULTIPLIER_MIN_CRIT, EasyDamageEngine.MULTIPLIER_MAX_CRIT);
	} else {
	    rDamage = new RandomRange(EasyDamageEngine.MULTIPLIER_MIN, EasyDamageEngine.MULTIPLIER_MAX);
	}
	final var multiplier = rDamage.generate();
	return (int) (rawDamage * multiplier / CommonDamageEngineParts.MULTIPLIER_DIVIDE);
    }

    private void didCrit() {
	this.crit = CommonDamageEngineParts.didSpecial(EasyDamageEngine.CRIT_CHANCE);
    }

    private void didFumble() {
	this.fumble = CommonDamageEngineParts.didSpecial(EasyDamageEngine.FUMBLE_CHANCE);
    }

    private void didPierce() {
	this.pierce = CommonDamageEngineParts.didSpecial(EasyDamageEngine.PIERCE_CHANCE);
    }

    @Override
    public boolean enemyDodged() {
	return this.dodged;
    }

    @Override
    public boolean weaponCrit() {
	return this.crit;
    }

    @Override
    public boolean weaponFumble() {
	return this.fumble;
    }

    @Override
    public boolean weaponMissed() {
	return this.missed;
    }

    @Override
    public boolean weaponPierce() {
	return this.pierce;
    }
}