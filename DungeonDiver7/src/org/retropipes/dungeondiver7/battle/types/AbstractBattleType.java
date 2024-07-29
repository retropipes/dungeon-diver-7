package org.retropipes.dungeondiver7.battle.types;

import org.retropipes.dungeondiver7.dungeon.objects.BattleCharacter;

public abstract class AbstractBattleType {
    public static AbstractBattleType createBattle() {
	return new RegularBattle();
    }

    public static AbstractBattleType createFinalBossBattle() {
	return new FinalBossBattle();
    }

    protected boolean boss = false;
    protected boolean finalBoss = false;

    public abstract BattleCharacter getBattlers();

    public final boolean isBossBattle() {
	return this.boss;
    }

    public final boolean isFinalBossBattle() {
	return this.finalBoss;
    }
}