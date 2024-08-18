/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.types;

import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.dungeon.objects.BattleCharacter;

class FinalBossBattle extends BattleType {
    // Fields
    final BattleCharacter monster;

    // Constructors
    public FinalBossBattle() {
	this.boss = true;
	this.finalBoss = true;
	this.monster = new BattleCharacter(MonsterFactory.getNewFinalBossInstance());
    }

    @Override
    public BattleCharacter getBattlers() {
	return this.monster;
    }
}
