/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle;

import com.puttysoftware.dungeondiver7.creature.AbstractCreature;

public abstract class AbstractBattle {
	// Constructors
	protected AbstractBattle() {
		// Do nothing
	}

	public abstract void battleDone();

	public abstract boolean castSpell();

	public abstract void displayActiveEffects();

	public abstract void displayBattleStats();

	public abstract void doBattle();

	public abstract void doBattleByProxy();

	public abstract void doBossBattle();

	public abstract void doFinalBossBattle();

	public abstract boolean doPlayerActions(final int actionType);

	public abstract void doResult();

	public abstract boolean drain();

	public abstract void endTurn();

	public abstract void executeNextAIAction();

	public abstract AbstractCreature getEnemy();

	public abstract boolean getLastAIActionResult();

	public abstract BattleResult getResult();

	public abstract boolean isWaitingForAI();

	public abstract void maintainEffects(final boolean player);

	// Generic Methods
	public abstract void resetGUI();

	public abstract void setResult(final int resultCode);

	public abstract void setStatusMessage(final String msg);

	public abstract boolean steal();

	public abstract boolean updatePosition(int x, int y);
}
