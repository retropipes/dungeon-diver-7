/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.ai.window;

import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.spell.Spell;

public abstract class WindowAI {
    public static final int ACTION_ATTACK = 0;
    public static final int ACTION_FLEE = 1;
    public static final int ACTION_CAST_SPELL = 2;
    public static final int ACTION_STEAL = 3;
    public static final int ACTION_DRAIN = 4;
    public static final int ACTION_USE_ITEM = 5;
    // Fields
    protected Spell spell;

    // Methods
    public abstract int getNextAction(Creature c);

    public final Spell getSpellToCast() {
        return this.spell;
    }

    public void newRoundHook() {
        // Do nothing
    }
}
