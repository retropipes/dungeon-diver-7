/*  DungeonRunnerII: An RPG
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.creature.AbstractCreature;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractBattleCharacter;

public class BattleCharacter extends AbstractBattleCharacter {
    // Constructors
    public BattleCharacter(final AbstractCreature newTemplate) {
	super(newTemplate);
    }
}
