/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle.types;

import com.puttysoftware.dungeondiver7.creature.monster.MonsterFactory;
import com.puttysoftware.dungeondiver7.dungeon.objects.BattleCharacter;

class BossBattle extends AbstractBattleType {
    // Fields
    final BattleCharacter monster;

    // Constructors
    public BossBattle() {
        this.boss = true;
        this.monster = new BattleCharacter(MonsterFactory.getNewBossInstance());
    }

    // Methods
    @Override
    public BattleCharacter getBattlers() {
        return this.monster;
    }
}
