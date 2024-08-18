/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.map;

import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.dungeon.abc.BattleCharacter;

public class MapBattle {
    // Fields
    private final BattleCharacter monster;

    // Constructors
    public MapBattle(final int rows, final int columns) {
	this.monster = new BattleCharacter(MonsterFactory.getNewMonsterInstance(), rows, columns);
    }

    // Methods
    public BattleCharacter getBattlers() {
	return this.monster;
    }
}
