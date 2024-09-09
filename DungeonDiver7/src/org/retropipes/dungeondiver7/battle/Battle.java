/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.battle;

import org.retropipes.dungeondiver7.creature.Creature;

public abstract class Battle {
    // Constructors
    protected Battle() {
	// Do nothing
    }

    public abstract void battleDone();

    public abstract boolean castSpell();

    public abstract void displayActiveEffects();

    public abstract void displayBattleStats();

    public abstract void doBattle();

    public abstract void doBattleByProxy();

    public abstract void doFinalBossBattle();

    public abstract boolean doPlayerActions(final BattleAction actionType);

    public abstract void doResult();

    public abstract boolean drain();

    public abstract void endTurn();

    public abstract void executeNextAIAction();

    public abstract Creature getEnemy();

    public abstract boolean getLastAIActionResult();

    public abstract BattleResult getResult();

    public abstract boolean isWaitingForAI();

    public abstract void maintainEffects(final boolean player);

    // Generic Methods
    public abstract void resetGUI();

    public abstract void setResult(final BattleResult resultCode);

    public abstract void setStatusMessage(final String msg);

    public abstract boolean steal();

    public abstract boolean updatePosition(int x, int y);
}
