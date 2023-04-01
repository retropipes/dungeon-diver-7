/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.monster;

import com.puttysoftware.dungeondiver7.ai.MapAIRoutinePicker;
import com.puttysoftware.dungeondiver7.creature.AbstractCreature;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.loader.BossImageManager;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.prefs.Prefs;
import com.puttysoftware.dungeondiver7.spell.SpellBook;
import com.puttysoftware.diane.assets.image.BufferedImageIcon;
import com.puttysoftware.diane.random.RandomRange;

public class FinalBossMonster extends AbstractMonster {
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

    // Constructors
    FinalBossMonster() {
	this.setMapAI(MapAIRoutinePicker.getNextRoutine());
	final SpellBook spells = new SystemMonsterSpellBook();
	spells.learnAllSpells();
	this.setSpellBook(spells);
	this.loadCreature();
    }

    // Methods
    @Override
    public boolean checkLevelUp() {
	return false;
    }

    @Override
    protected void levelUpHook() {
	// Do nothing
    }

    @Override
    protected BufferedImageIcon getInitialImage() {
	return BossImageManager.getFinalBossImage();
    }

    @Override
    public int getSpeed() {
	final var difficulty = Prefs.getGameDifficulty();
	final var base = this.getBaseSpeed();
	if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_SLOWEST);
	}
	if (difficulty == Prefs.DIFFICULTY_EASY) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_SLOW);
	}
	if (difficulty == Prefs.DIFFICULTY_NORMAL) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
	}
	if (difficulty == Prefs.DIFFICULTY_HARD) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_FAST);
	}
	if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_FASTEST);
	}
	return (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
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

    private int getInitialStrength() {
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

    private int getInitialAgility() {
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

    private static int getStatMultiplierForDifficulty() {
	final var difficulty = Prefs.getGameDifficulty();
	if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
	    return FinalBossMonster.STAT_MULT_VERY_EASY;
	}
	if (difficulty == Prefs.DIFFICULTY_EASY) {
	    return FinalBossMonster.STAT_MULT_EASY;
	}
	if (difficulty == Prefs.DIFFICULTY_NORMAL) {
	    return FinalBossMonster.STAT_MULT_NORMAL;
	}
	if (difficulty == Prefs.DIFFICULTY_HARD) {
	    return FinalBossMonster.STAT_MULT_HARD;
	}
	if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
	    return FinalBossMonster.STAT_MULT_VERY_HARD;
	}
	return FinalBossMonster.STAT_MULT_NORMAL;
    }

    private static int getMinimumStatForDifficulty() {
	final var difficulty = Prefs.getGameDifficulty();
	if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
	    return FinalBossMonster.MINIMUM_STAT_VALUE_VERY_EASY;
	}
	if (difficulty == Prefs.DIFFICULTY_EASY) {
	    return FinalBossMonster.MINIMUM_STAT_VALUE_EASY;
	}
	if (difficulty == Prefs.DIFFICULTY_NORMAL) {
	    return FinalBossMonster.MINIMUM_STAT_VALUE_NORMAL;
	}
	if (difficulty == Prefs.DIFFICULTY_HARD) {
	    return FinalBossMonster.MINIMUM_STAT_VALUE_HARD;
	}
	if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
	    return FinalBossMonster.MINIMUM_STAT_VALUE_VERY_HARD;
	}
	return FinalBossMonster.MINIMUM_STAT_VALUE_NORMAL;
    }
}
