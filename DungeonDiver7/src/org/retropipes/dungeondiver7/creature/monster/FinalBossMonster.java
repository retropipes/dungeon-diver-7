/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.creature.spell.SpellBook;
import org.retropipes.dungeondiver7.loader.image.monster.MonsterImageLoader;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.utility.GameDifficulty;

public class FinalBossMonster extends Monster {
    // Fields
    private static final int MINIMUM_STAT_VALUE_VERY_EASY = 50;
    private static final int MINIMUM_STAT_VALUE_EASY = 60;
    private static final int MINIMUM_STAT_VALUE_NORMAL = 70;
    private static final int MINIMUM_STAT_VALUE_HARD = 80;
    private static final int MINIMUM_STAT_VALUE_VERY_HARD = 90;
    private static final int STAT_MULT_VERY_EASY = 5;
    private static final int STAT_MULT_EASY = 6;
    private static final int STAT_MULT_NORMAL = 7;
    private static final int STAT_MULT_HARD = 8;
    private static final int STAT_MULT_VERY_HARD = 9;

    private static int getMinimumStatForDifficulty() {
	final var difficulty = Prefs.getGameDifficulty();
	switch (difficulty) {
	case GameDifficulty.VERY_EASY:
	    return FinalBossMonster.MINIMUM_STAT_VALUE_VERY_EASY;
	case GameDifficulty.EASY:
	    return FinalBossMonster.MINIMUM_STAT_VALUE_EASY;
	case GameDifficulty.NORMAL:
	    return FinalBossMonster.MINIMUM_STAT_VALUE_NORMAL;
	case GameDifficulty.HARD:
	    return FinalBossMonster.MINIMUM_STAT_VALUE_HARD;
	case GameDifficulty.VERY_HARD:
	    return FinalBossMonster.MINIMUM_STAT_VALUE_VERY_HARD;
	default:
	    break;
	}
	return FinalBossMonster.MINIMUM_STAT_VALUE_NORMAL;
    }

    private static int getStatMultiplierForDifficulty() {
	final var difficulty = Prefs.getGameDifficulty();
	switch (difficulty) {
	case GameDifficulty.VERY_EASY:
	    return FinalBossMonster.STAT_MULT_VERY_EASY;
	case GameDifficulty.EASY:
	    return FinalBossMonster.STAT_MULT_EASY;
	case GameDifficulty.NORMAL:
	    return FinalBossMonster.STAT_MULT_NORMAL;
	case GameDifficulty.HARD:
	    return FinalBossMonster.STAT_MULT_HARD;
	case GameDifficulty.VERY_HARD:
	    return FinalBossMonster.STAT_MULT_VERY_HARD;
	default:
	    break;
	}
	return FinalBossMonster.STAT_MULT_NORMAL;
    }

    // Constructors
    FinalBossMonster() {
	final SpellBook spells = new MonsterSpellBook();
	spells.learnAllSpells();
	this.setSpellBook(spells);
	this.loadCreature();
    }

    @Override
    public boolean checkLevelUp() {
	return false;
    }

    private int getInitialAgility() {
	final var min = FinalBossMonster.getMinimumStatForDifficulty();
	final var r = new RandomRange(min,
		Math.max(this.getLevel() * FinalBossMonster.getStatMultiplierForDifficulty(), min));
	return r.generate();
    }

    private int getInitialBlock() {
	final var min = FinalBossMonster.getMinimumStatForDifficulty();
	final var r = new RandomRange(min,
		Math.max(this.getLevel() * FinalBossMonster.getStatMultiplierForDifficulty(), min));
	return r.generate();
    }

    @Override
    protected BufferedImageIcon getInitialImage() {
	return MonsterImageLoader.loadFinalBoss();
    }

    private int getInitialIntelligence() {
	final var min = FinalBossMonster.getMinimumStatForDifficulty();
	final var r = new RandomRange(min,
		Math.max(this.getLevel() * FinalBossMonster.getStatMultiplierForDifficulty(), min));
	return r.generate();
    }

    private int getInitialLuck() {
	final var min = FinalBossMonster.getMinimumStatForDifficulty();
	final var r = new RandomRange(min,
		Math.max(this.getLevel() * FinalBossMonster.getStatMultiplierForDifficulty(), min));
	return r.generate();
    }

    private int getInitialStrength() {
	final var min = FinalBossMonster.getMinimumStatForDifficulty();
	final var r = new RandomRange(min,
		Math.max(this.getLevel() * FinalBossMonster.getStatMultiplierForDifficulty(), min));
	return r.generate();
    }

    private int getInitialVitality() {
	final var min = FinalBossMonster.getMinimumStatForDifficulty();
	final var r = new RandomRange(min,
		Math.max(this.getLevel() * FinalBossMonster.getStatMultiplierForDifficulty(), min));
	return r.generate();
    }

    @Override
    public int getSpeed(GameDifficulty difficulty) {
	final var base = this.getBaseSpeed();
	switch (difficulty) {
	case GameDifficulty.VERY_EASY:
	    return (int) (base * Creature.SPEED_ADJUST_SLOWEST);
	case GameDifficulty.EASY:
	    return (int) (base * Creature.SPEED_ADJUST_SLOW);
	case GameDifficulty.NORMAL:
	    return (int) (base * Creature.SPEED_ADJUST_NORMAL);
	case GameDifficulty.HARD:
	    return (int) (base * Creature.SPEED_ADJUST_FAST);
	case GameDifficulty.VERY_HARD:
	    return (int) (base * Creature.SPEED_ADJUST_FASTEST);
	default:
	    break;
	}
	return (int) (base * Creature.SPEED_ADJUST_NORMAL);
    }

    @Override
    protected void levelUpHook() {
	// Do nothing
    }

    // Helper Methods
    @Override
    public void loadCreature() {
	final var zoneID = PartyManager.getParty().getZone();
	final var bossName = Strings.boss(zoneID);
	this.overrideDefaults(zoneID, bossName);
	final var newLevel = zoneID + 6;
	this.setLevel(newLevel);
	this.setVitality(this.getInitialVitality());
	this.setCurrentHP(this.getMaximumHP());
	this.setIntelligence(this.getInitialIntelligence());
	this.setCurrentMP(this.getMaximumMP());
	this.setStrength(this.getInitialStrength());
	this.setBlock(this.getInitialBlock());
	this.setAgility(this.getInitialAgility());
	this.setLuck(this.getInitialLuck());
	this.setGold(0);
	this.setExperience(0);
	this.setAttacksPerRound(1);
	this.setSpellsPerRound(1);
	this.image = this.getInitialImage();
    }
}
