/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle.ai.window;

import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.battle.ai.AIContext;
import org.retropipes.dungeondiver7.battle.ai.CreatureAI;

public abstract class WindowAI extends CreatureAI {
    @Override
    public final BattleAction getNextAction(AIContext ac) {
	return getNextAction(ac.getCreature()); // Not used
    }
}
