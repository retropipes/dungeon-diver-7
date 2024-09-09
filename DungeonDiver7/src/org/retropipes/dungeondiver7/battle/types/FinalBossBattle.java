/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.types;

import org.retropipes.dungeondiver7.battle.BattleCharacter;
import org.retropipes.dungeondiver7.battle.ai.map.MapAIPicker;
import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.settings.Settings;

class FinalBossBattle extends BattleType {
    // Fields
    final BattleCharacter monster;

    // Constructors
    public FinalBossBattle(final int rows, final int columns) {
	this.finalBoss = true;
	this.monster = new BattleCharacter(MonsterFactory.getNewFinalBossInstance(Settings.getGameDifficulty()), rows,
		columns);
	this.monster.setAI(MapAIPicker.getNextRoutine());
    }

    @Override
    public BattleCharacter getBattlers() {
	return this.monster;
    }
}
