/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import java.util.Objects;

import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.ai.AbstractMapAIRoutine;
import org.retropipes.dungeondiver7.ai.MapAIRoutinePicker;
import org.retropipes.dungeondiver7.creature.AbstractCreature;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.names.Monsters;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.spell.SpellBook;

public abstract class AbstractMonster extends AbstractCreature {
    protected static final double MINIMUM_EXPERIENCE_RANDOM_VARIANCE = -5.0 / 2.0;
    protected static final double MAXIMUM_EXPERIENCE_RANDOM_VARIANCE = 5.0 / 2.0;
    protected static final int PERFECT_GOLD_MIN = 1;
    protected static final int PERFECT_GOLD_MAX = 3;
    private static final int BATTLES_SCALE_FACTOR = 2;
    private static final int BATTLES_START = 2;

    // Helper Methods
    private static AbstractMapAIRoutine getInitialMapAI() {
	return MapAIRoutinePicker.getNextRoutine();
    }

    // Fields
    private String type;
    private int monID;

    // Constructors
    AbstractMonster() {
	super(1);
	this.setMapAI(AbstractMonster.getInitialMapAI());
	final SpellBook spells = new SystemMonsterSpellBook();
	spells.learnAllSpells();
	this.setSpellBook(spells);
    }

    protected double adjustForLevelDifference() {
	return Math.max(0.0, this.getLevelDifference() / 4.0 + 1.0);
    }

    @Override
    public boolean checkLevelUp() {
	return false;
    }

    protected void configureDefaults() {
	this.monID = RandomRange.generate(0, 99);
	final var zoneID = PartyManager.getParty().getZone();
	this.type = Monsters.getType(zoneID, this.monID);
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj) || !(obj instanceof final AbstractMonster other)
		|| !Objects.equals(this.type, other.type)) {
	    return false;
	}
	return true;
    }

    protected final int getBattlesToNextLevel() {
	return AbstractMonster.BATTLES_START + (this.getLevel() + 1) * AbstractMonster.BATTLES_SCALE_FACTOR;
    }

    @Override
    protected final int getInitialPerfectBonusGold() {
	final var tough = this.getToughness();
	final var min = tough * AbstractMonster.PERFECT_GOLD_MIN;
	final var max = tough * AbstractMonster.PERFECT_GOLD_MAX;
	final var r = new RandomRange(min, max);
	return (int) (r.generate() * this.adjustForLevelDifference());
    }

    public final int getMonsterID() {
	return this.monID;
    }

    @Override
    public String getName() {
	return this.type;
    }

    @Override
    public int getSpeed() {
	final var difficulty = Prefs.getGameDifficulty();
	final var base = this.getBaseSpeed();
	return switch (difficulty) {
	case Prefs.DIFFICULTY_VERY_EASY -> (int) (base * AbstractCreature.SPEED_ADJUST_SLOWEST);
	case Prefs.DIFFICULTY_EASY -> (int) (base * AbstractCreature.SPEED_ADJUST_SLOW);
	case Prefs.DIFFICULTY_NORMAL -> (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
	case Prefs.DIFFICULTY_HARD -> (int) (base * AbstractCreature.SPEED_ADJUST_FAST);
	case Prefs.DIFFICULTY_VERY_HARD -> (int) (base * AbstractCreature.SPEED_ADJUST_FASTEST);
	default -> (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
	};
    }

    private int getToughness() {
	return this.getStrength() + this.getBlock() + this.getAgility() + this.getVitality() + this.getIntelligence()
		+ this.getLuck();
    }

    final String getType() {
	return this.type;
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	final var result = super.hashCode();
	return prime * result + (this.type == null ? 0 : this.type.hashCode());
    }

    @Override
    protected void levelUpHook() {
	// Do nothing
    }

    protected void overrideDefaults(final int oID, final String oType) {
	this.monID = oID;
	this.type = oType;
    }
}
