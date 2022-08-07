package com.puttysoftware.dungeondiver7.battle.types;

import com.puttysoftware.dungeondiver7.dungeon.objects.BattleCharacter;

public abstract class AbstractBattleType {
    protected boolean boss = false;
    protected boolean finalBoss = false;

    public abstract BattleCharacter getBattlers();

    public final boolean isBossBattle() {
	return this.boss;
    }

    public final boolean isFinalBossBattle() {
	return this.finalBoss;
    }

    public static AbstractBattleType createBattle() {
	return new RegularBattle();
    }

    public static AbstractBattleType createBossBattle() {
	return new BossBattle();
    }

    public static AbstractBattleType createFinalBossBattle() {
	return new FinalBossBattle();
    }
}