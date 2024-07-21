/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.reward;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

class BossBattleRewards {
	// Fields
	static final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };

	public static void doRewards(final BattleResult br) {
		final var player = PartyManager.getParty().getLeader();
		player.healAndRegenerateFully();
		if (br == BattleResult.LOST) {
			player.offsetExperiencePercentage(-10);
			player.offsetGoldPercentage(-100);
		} else if (br == BattleResult.ANNIHILATED) {
			player.offsetExperiencePercentage(-20);
			player.offsetGoldPercentage(-100);
		} else if (br == BattleResult.WON || br == BattleResult.PERFECT) {
			SoundLoader.playSound(Sounds.BOSS_DIE);
			// Send player to next zone
			DungeonDiver7.getStuffBag().getGameLogic().goToLevelOffset(1);
		}
	}

	// Constructor
	private BossBattleRewards() {
		// Do nothing
	}
}
