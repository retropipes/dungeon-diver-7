/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.monster;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.random.RandomLongRange;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.asset.BossImageManager;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.prefs.Prefs;

class BossMonster extends AbstractMonster {
	// Constants
	private static final int STAT_MULT_VERY_EASY = 4;
	private static final int STAT_MULT_EASY = 5;
	private static final int STAT_MULT_NORMAL = 6;
	private static final int STAT_MULT_HARD = 7;
	private static final int STAT_MULT_VERY_HARD = 8;
	private static final double EXP_MULT_VERY_EASY = 1.6;
	private static final double EXP_MULT_EASY = 1.4;
	private static final double EXP_MULT_NORMAL = 1.2;
	private static final double EXP_MULT_HARD = 1.0;
	private static final double EXP_MULT_VERY_HARD = 0.8;

	private static double getExpMultiplierForDifficulty() {
		final var difficulty = Prefs.getGameDifficulty();
		if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
			return BossMonster.EXP_MULT_VERY_EASY;
		}
		if (difficulty == Prefs.DIFFICULTY_EASY) {
			return BossMonster.EXP_MULT_EASY;
		}
		if (difficulty == Prefs.DIFFICULTY_NORMAL) {
			return BossMonster.EXP_MULT_NORMAL;
		}
		if (difficulty == Prefs.DIFFICULTY_HARD) {
			return BossMonster.EXP_MULT_HARD;
		}
		if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
			return BossMonster.EXP_MULT_VERY_HARD;
		}
		return BossMonster.EXP_MULT_NORMAL;
	}

	private static int getInitialGold() {
		return 0;
	}

	private static int getStatMultiplierForDifficulty() {
		final var difficulty = Prefs.getGameDifficulty();
		if (difficulty == Prefs.DIFFICULTY_VERY_EASY) {
			return BossMonster.STAT_MULT_VERY_EASY;
		}
		if (difficulty == Prefs.DIFFICULTY_EASY) {
			return BossMonster.STAT_MULT_EASY;
		}
		if (difficulty == Prefs.DIFFICULTY_NORMAL) {
			return BossMonster.STAT_MULT_NORMAL;
		}
		if (difficulty == Prefs.DIFFICULTY_HARD) {
			return BossMonster.STAT_MULT_HARD;
		}
		if (difficulty == Prefs.DIFFICULTY_VERY_HARD) {
			return BossMonster.STAT_MULT_VERY_HARD;
		}
		return BossMonster.STAT_MULT_NORMAL;
	}

	// Constructors
	BossMonster() {
		this.image = this.getInitialImage();
	}

	private int getInitialAgility() {
		final var r = new RandomRange(1, Math.max(this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), 1));
		return r.generate();
	}

	private int getInitialBlock() {
		final var r = new RandomRange(0, this.getLevel() * BossMonster.getStatMultiplierForDifficulty());
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
				+ r.generate() * this.adjustForLevelDifference() * BossMonster.getExpMultiplierForDifficulty());
	}

	@Override
	protected BufferedImageIcon getInitialImage() {
		final var zoneID = PartyManager.getParty().getZone();
		return BossImageManager.getBossImage(zoneID);
	}

	private int getInitialIntelligence() {
		final var r = new RandomRange(0, this.getLevel() * BossMonster.getStatMultiplierForDifficulty());
		return r.generate();
	}

	private int getInitialLuck() {
		final var r = new RandomRange(0, this.getLevel() * BossMonster.getStatMultiplierForDifficulty());
		return r.generate();
	}

	private int getInitialStrength() {
		final var r = new RandomRange(1, Math.max(this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), 1));
		return r.generate();
	}

	private int getInitialVitality() {
		final var r = new RandomRange(1, Math.max(this.getLevel() * BossMonster.getStatMultiplierForDifficulty(), 1));
		return r.generate();
	}

	@Override
	public void loadCreature() {
		final var zoneID = PartyManager.getParty().getZone();
		final var bossName = Strings.boss(zoneID);
		this.overrideDefaults(zoneID, bossName);
		final var newLevel = zoneID + 1;
		this.setLevel(newLevel);
		this.setVitality(this.getInitialVitality());
		this.setCurrentHP(this.getMaximumHP());
		this.setIntelligence(this.getInitialIntelligence());
		this.setCurrentMP(this.getMaximumMP());
		this.setStrength(this.getInitialStrength());
		this.setBlock(this.getInitialBlock());
		this.setAgility(this.getInitialAgility());
		this.setLuck(this.getInitialLuck());
		this.setGold(BossMonster.getInitialGold());
		this.setExperience((long) (this.getInitialExperience() * this.adjustForLevelDifference()));
		this.setAttacksPerRound(1);
		this.setSpellsPerRound(1);
		this.image = this.getInitialImage();
	}
}
