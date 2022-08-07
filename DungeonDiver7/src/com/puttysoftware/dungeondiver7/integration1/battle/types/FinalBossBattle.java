/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.battle.types;

import com.puttysoftware.dungeondiver7.integration1.creature.monster.MonsterFactory;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.BattleCharacter;

class FinalBossBattle extends AbstractBattleType {
    // Fields
    final BattleCharacter monster;

    // Constructors
    public FinalBossBattle() {
	super();
	this.boss = true;
	this.finalBoss = true;
	this.monster = new BattleCharacter(MonsterFactory.getNewFinalBossInstance());
    }

    // Methods
    @Override
    public BattleCharacter getBattlers() {
	return this.monster;
    }
}
