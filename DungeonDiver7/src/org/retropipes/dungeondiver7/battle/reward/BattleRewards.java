/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.reward;

import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.battle.types.BattleType;

public abstract class BattleRewards {
    public static void doRewards(final BattleType bt, final BattleResult br, final long bonusExp, final int bonusGold) {
	if (bt.isFinalBossBattle()) {
	    FinalBossBattleRewards.doRewards(br);
	} else {
	    RegularBattleRewards.doRewards(br, bonusExp, bonusGold);
	}
    }
}