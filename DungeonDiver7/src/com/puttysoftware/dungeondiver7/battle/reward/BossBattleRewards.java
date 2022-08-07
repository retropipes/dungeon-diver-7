/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle.reward;

import com.puttysoftware.dungeondiver7.battle.BattleResult;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.creature.party.PartyMember;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundManager;

class BossBattleRewards {
    // Fields
    static final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };

    // Constructor
    private BossBattleRewards() {
	// Do nothing
    }

    // Methods
    public static void doRewards(final BattleResult br) {
	final PartyMember player = PartyManager.getParty().getLeader();
	player.healAndRegenerateFully();
	if (br == BattleResult.LOST) {
	    player.offsetExperiencePercentage(-10);
	    player.offsetGoldPercentage(-100);
	} else if (br == BattleResult.ANNIHILATED) {
	    player.offsetExperiencePercentage(-20);
	    player.offsetGoldPercentage(-100);
	} else if (br == BattleResult.WON || br == BattleResult.PERFECT) {
	    SoundManager.playSound(SoundConstants.BOSS_DIE);
	    // Send player to next zone
	    Integration1.getApplication().getGameLogic().goToLevelOffset(1);
	}
    }
}
