/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.reward;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

class FinalBossBattleRewards {
    // Fields
    static final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };

    public static void doRewards(final BattleResult br) {
	final var player = PartyManager.getParty().getLeader();
	if (br == BattleResult.WON || br == BattleResult.PERFECT) {
	    SoundLoader.playSound(Sounds.WIN_GAME);
	    int dialogResult = CommonDialogs.CANCEL;
	    while (dialogResult == CommonDialogs.CANCEL) {
		dialogResult = CommonDialogs.showCustomDialogWithDefault(
			"You get to increase a stat permanently.\nWhich Stat?", "Boss Rewards",
			FinalBossBattleRewards.rewardOptions, FinalBossBattleRewards.rewardOptions[0]);
	    }
	    if (dialogResult == 0) {
		// Attack
		player.spendPointOnAttack();
	    } else if (dialogResult == 1) {
		// Defense
		player.spendPointOnDefense();
	    } else if (dialogResult == 2) {
		// HP
		player.spendPointOnHP();
	    } else if (dialogResult == 3) {
		// MP
		player.spendPointOnMP();
	    }
	    PartyManager.updatePostKill();
	} else {
	    player.healAndRegenerateFully();
	}
    }

    // Constructor
    private FinalBossBattleRewards() {
	// Do nothing
    }
}
