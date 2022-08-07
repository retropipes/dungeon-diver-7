/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle.reward;

import javax.swing.JOptionPane;

import com.puttysoftware.dungeondiver7.battle.BattleResult;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.creature.party.PartyMember;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundManager;

class FinalBossBattleRewards {
    // Fields
    static final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };

    // Constructor
    private FinalBossBattleRewards() {
	// Do nothing
    }

    // Methods
    public static void doRewards(final BattleResult br) {
	final PartyMember player = PartyManager.getParty().getLeader();
	if (br == BattleResult.WON || br == BattleResult.PERFECT) {
	    SoundManager.playSound(SoundConstants.WIN_GAME);
	    String dialogResult = null;
	    while (dialogResult == null) {
		dialogResult = (String) JOptionPane.showInputDialog(null,
			"You get to increase a stat permanently.\nWhich Stat?", "Boss Rewards",
			JOptionPane.QUESTION_MESSAGE, null, FinalBossBattleRewards.rewardOptions,
			FinalBossBattleRewards.rewardOptions[0]);
	    }
	    if (dialogResult.equals(FinalBossBattleRewards.rewardOptions[0])) {
		// Attack
		player.spendPointOnAttack();
	    } else if (dialogResult.equals(FinalBossBattleRewards.rewardOptions[1])) {
		// Defense
		player.spendPointOnDefense();
	    } else if (dialogResult.equals(FinalBossBattleRewards.rewardOptions[2])) {
		// HP
		player.spendPointOnHP();
	    } else if (dialogResult.equals(FinalBossBattleRewards.rewardOptions[3])) {
		// MP
		player.spendPointOnMP();
	    }
	    PartyManager.updatePostKill();
	} else {
	    player.healAndRegenerateFully();
	}
    }
}
