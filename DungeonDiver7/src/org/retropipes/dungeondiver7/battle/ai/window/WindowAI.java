/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.ai.window;

import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.battle.ai.AIContext;
import org.retropipes.dungeondiver7.creature.CreatureAI;

public abstract class WindowAI extends CreatureAI {
    @Override
    public final BattleAction getNextAction(AIContext ac) {
	return getNextAction(ac.getCreature()); // Not used
    }
}
