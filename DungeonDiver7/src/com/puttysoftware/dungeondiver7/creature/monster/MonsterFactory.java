/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.monster;

import com.puttysoftware.dungeondiver7.creature.AbstractCreature;

public class MonsterFactory {
    private MonsterFactory() {
        // Do nothing
    }

    public static AbstractCreature getNewMonsterInstance() {
        return new Monster();
    }

    public static AbstractCreature getNewBossInstance() {
        return new BossMonster();
    }

    public static AbstractCreature getNewFinalBossInstance() {
        return new FinalBossMonster();
    }
}
