/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.random.RandomLongRange;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.loader.MonsterImageManager;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.shop.Shop;

class Monster extends AbstractMonster {
	// Constants
	private static final int STAT_MULT_VERY_EASY = 3;
	private static final int STAT_MULT_EASY = 4;
	private static final int STAT_MULT_NORMAL = 5;
	private static final int STAT_MULT_HARD = 6;
	private static final int STAT_MULT_VERY_HARD = 7;
	private static final double GOLD_MULT_VERY_EASY = 2.0;
	private static final double GOLD_MULT_EASY = 1.5;
	private static final double GOLD_MULT_NORMAL = 1.0;
	private static final double GOLD_MULT_HARD = 0.75;
	private static final double GOLD_MULT_VERY_HARD = 0.5;
	private static final double EXP_MULT_VERY_EASY = 1.2;
	private static final double EXP_MULT_EASY = 1.1;
	private static final double EXP_MULT_NORMAL = 1.0;
	private static final double EXP_MULT_HARD = 0.9;
	private static final double EXP_MULT_VERY_HARD = 0.8;

	private static double getExpMultiplierForDifficulty() {
		final var difficulty = Prefs.getGameDifficulty();
		return switch (difficulty) {
		case Prefs.DIFFICULTY_VERY_EASY -> Monster.EXP_MULT_VERY_EASY;
		case Prefs.DIFFICULTY_EASY -> Monster.EXP_MULT_EASY;
		case Prefs.DIFFICULTY_NORMAL -> Monster.EXP_MULT_NORMAL;
		case Prefs.DIFFICULTY_HARD -> Monster.EXP_MULT_HARD;
		case Prefs.DIFFICULTY_VERY_HARD -> Monster.EXP_MULT_VERY_HARD;
		default -> Monster.EXP_MULT_NORMAL;
		};
	}

	private static double getGoldMultiplierForDifficulty() {
		final var difficulty = Prefs.getGameDifficulty();
		return switch (difficulty) {
		case Prefs.DIFFICULTY_VERY_EASY -> Monster.GOLD_MULT_VERY_EASY;
		case Prefs.DIFFICULTY_EASY -> Monster.GOLD_MULT_EASY;
		case Prefs.DIFFICULTY_NORMAL -> Monster.GOLD_MULT_NORMAL;
		case Prefs.DIFFICULTY_HARD -> Monster.GOLD_MULT_HARD;
		case Prefs.DIFFICULTY_VERY_HARD -> Monster.GOLD_MULT_VERY_HARD;
		default -> Monster.GOLD_MULT_NORMAL;
		};
	}

	private static int getStatMultiplierForDifficulty() {
		final var difficulty = Prefs.getGameDifficulty();
		return switch (difficulty) {
		case Prefs.DIFFICULTY_VERY_EASY -> Monster.STAT_MULT_VERY_EASY;
		case Prefs.DIFFICULTY_EASY -> Monster.STAT_MULT_EASY;
		case Prefs.DIFFICULTY_NORMAL -> Monster.STAT_MULT_NORMAL;
		case Prefs.DIFFICULTY_HARD -> Monster.STAT_MULT_HARD;
		case Prefs.DIFFICULTY_VERY_HARD -> Monster.STAT_MULT_VERY_HARD;
		default -> Monster.STAT_MULT_NORMAL;
		};
	}

	// Constructors
	Monster() {
		this.image = this.getInitialImage();
	}

	private int getInitialAgility() {
		final var r = new RandomRange(1, Math.max(this.getLevel() * Monster.getStatMultiplierForDifficulty(), 1));
		return r.generate();
	}

	private int getInitialBlock() {
		final var r = new RandomRange(0, this.getLevel() * Monster.getStatMultiplierForDifficulty());
		return r.generate();
	}

	private long getInitialExperience() {
		int minvar, maxvar;
		minvar = (int) (this.getLevel() * AbstractMonster.MINIMUM_EXPERIENCE_RANDOM_VARIANCE);
		maxvar = (int) (this.getLevel() * AbstractMonster.MAXIMUM_EXPERIENCE_RANDOM_VARIANCE);
		final var r = new RandomLongRange(minvar, maxvar);
		final var expbase = PartyManager.getParty().getPartyMaxToNextLevel();
		final long factor = this.getBattlesToNextLevel();
		return (int) (expbase / factor
				+ r.generate() * this.adjustForLevelDifference() * Monster.getExpMultiplierForDifficulty());
	}

	private int getInitialGold() {
		final var playerCharacter = PartyManager.getParty().getLeader();
		final var needed = Shop.getEquipmentCost(playerCharacter.getLevel() + 1) * 4;
		final var factor = this.getBattlesToNextLevel();
		final var min = 0;
		final var max = needed / factor * 2;
		final var r = new RandomRange(min, max);
		return (int) (r.generate() * this.adjustForLevelDifference() * Monster.getGoldMultiplierForDifficulty());
	}

	@Override
	protected BufferedImageIcon getInitialImage() {
		final var monID = this.getMonsterID();
		return MonsterImageManager.getImage(monID);
	}

	private int getInitialIntelligence() {
		final var r = new RandomRange(0, this.getLevel() * Monster.getStatMultiplierForDifficulty());
		return r.generate();
	}

	private int getInitialLuck() {
		final var r = new RandomRange(0, this.getLevel() * Monster.getStatMultiplierForDifficulty());
		return r.generate();
	}

	private int getInitialStrength() {
		final var r = new RandomRange(1, Math.max(this.getLevel() * Monster.getStatMultiplierForDifficulty(), 1));
		return r.generate();
	}

	private int getInitialVitality() {
		final var r = new RandomRange(1, Math.max(this.getLevel() * Monster.getStatMultiplierForDifficulty(), 1));
		return r.generate();
	}

	@Override
	public void loadCreature() {
		this.configureDefaults();
		final var newLevel = PartyManager.getParty().getZone() + 1;
		this.setLevel(newLevel);
		this.setVitality(this.getInitialVitality());
		this.setCurrentHP(this.getMaximumHP());
		this.setIntelligence(this.getInitialIntelligence());
		this.setCurrentMP(this.getMaximumMP());
		this.setStrength(this.getInitialStrength());
		this.setBlock(this.getInitialBlock());
		this.setAgility(this.getInitialAgility());
		this.setLuck(this.getInitialLuck());
		this.setGold(this.getInitialGold());
		this.setExperience((long) (this.getInitialExperience() * this.adjustForLevelDifference()));
		this.setAttacksPerRound(1);
		this.setSpellsPerRound(1);
		this.image = this.getInitialImage();
	}
}
