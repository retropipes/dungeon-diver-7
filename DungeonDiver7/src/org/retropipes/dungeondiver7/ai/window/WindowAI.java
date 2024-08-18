/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.ai.window;

import org.retropipes.dungeondiver7.ai.BattleAction;
import org.retropipes.dungeondiver7.ai.AIContext;
import org.retropipes.dungeondiver7.ai.CreatureAI;

public abstract class WindowAI extends CreatureAI {
    @Override
    public final BattleAction getNextAction(AIContext ac) {
	return getNextAction(ac.getCreature()); // Not used
    }
}
