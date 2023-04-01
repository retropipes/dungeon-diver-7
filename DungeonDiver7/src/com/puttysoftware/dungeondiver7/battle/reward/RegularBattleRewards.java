/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle.reward;

import com.puttysoftware.dungeondiver7.battle.BattleResult;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;

class RegularBattleRewards {
    // Fields
    static final String[] rewardOptions = { "Attack", "Defense", "HP", "MP" };

    // Constructor
    private RegularBattleRewards() {
        // Do nothing
    }

    public static void doRewards(final BattleResult br, final long baseExp, final int baseGold) {
        RegularBattleRewards.processExp(br, baseExp);
        RegularBattleRewards.processGold(br, baseGold);
    }

    private static void processGold(final BattleResult br, final int baseGold) {
        final var player = PartyManager.getParty().getLeader();
        if (br == BattleResult.WON || br == BattleResult.PERFECT) {
            player.offsetGold(baseGold);
        } else if (br == BattleResult.LOST || br == BattleResult.ANNIHILATED) {
            player.offsetGoldPercentage(-100);
        }
    }

    private static void processExp(final BattleResult br, final long baseExp) {
        final var player = PartyManager.getParty().getLeader();
        if (br == BattleResult.PERFECT) {
            player.offsetExperience(baseExp * 6 / 5);
        } else if (br == BattleResult.LOST) {
            player.offsetExperiencePercentage(-10);
            player.healAndRegenerateFully();
        } else if (br == BattleResult.ANNIHILATED) {
            player.offsetExperiencePercentage(-20);
            player.healAndRegenerateFully();
        } else if (br == BattleResult.WON) {
            player.offsetExperience(baseExp);
        }
    }
}
