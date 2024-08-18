/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.types;

import org.retropipes.dungeondiver7.battle.ai.map.MapAIPicker;
import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.dungeon.abc.BattleCharacter;

class FinalBossBattle extends BattleType {
    // Fields
    final BattleCharacter monster;

    // Constructors
    public FinalBossBattle(final int rows, final int columns) {
	this.finalBoss = true;
	this.monster = new BattleCharacter(MonsterFactory.getNewFinalBossInstance(), rows, columns);
	this.monster.setAI(MapAIPicker.getNextRoutine());
    }

    @Override
    public BattleCharacter getBattlers() {
	return this.monster;
    }
}
