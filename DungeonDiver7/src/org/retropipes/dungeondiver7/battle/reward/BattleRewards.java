package org.retropipes.dungeondiver7.battle.reward;

import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.battle.types.BattleType;

public abstract class BattleRewards {
    public static void doRewards(final BattleType bt, final BattleResult br, final long bonusExp,
	    final int bonusGold) {
	if (bt.isFinalBossBattle()) {
	    FinalBossBattleRewards.doRewards(br);
	} else {
	    RegularBattleRewards.doRewards(br, bonusExp, bonusGold);
	}
    }
}