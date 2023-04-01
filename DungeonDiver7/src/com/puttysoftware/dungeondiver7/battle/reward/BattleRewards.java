package com.puttysoftware.dungeondiver7.battle.reward;

import com.puttysoftware.dungeondiver7.battle.BattleResult;
import com.puttysoftware.dungeondiver7.battle.types.AbstractBattleType;

public abstract class BattleRewards {
    public static void doRewards(final AbstractBattleType bt, final BattleResult br, final long bonusExp,
            final int bonusGold) {
        if (bt.isFinalBossBattle()) {
            FinalBossBattleRewards.doRewards(br);
        } else if (bt.isBossBattle()) {
            BossBattleRewards.doRewards(br);
        } else {
            RegularBattleRewards.doRewards(br, bonusExp, bonusGold);
        }
    }
}