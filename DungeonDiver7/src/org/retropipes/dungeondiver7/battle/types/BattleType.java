package org.retropipes.dungeondiver7.battle.types;

import org.retropipes.dungeondiver7.battle.BattleCharacter;

public abstract class BattleType {
    public static BattleType createBattle(final int rows, final int columns) {
	return new RegularBattle(rows, columns);
    }

    public static BattleType createFinalBossBattle(final int rows, final int columns) {
	return new FinalBossBattle(rows, columns);
    }

    protected boolean finalBoss = false;

    public abstract BattleCharacter getBattlers();

    public final boolean isFinalBossBattle() {
	return this.finalBoss;
    }
}