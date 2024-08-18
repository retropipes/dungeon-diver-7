/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.map;

import org.retropipes.dungeondiver7.ai.map.MapAIContext;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.objects.BattleCharacter;

public class MapBattleDefinitions {
    private static final int MAX_BATTLERS = 100;
    // Fields
    private BattleCharacter activeCharacter;
    private final BattleCharacter[] battlers;
    private final MapAIContext[] aiContexts;
    private Dungeon battleMap;
    private int battlerCount;

    // Constructors
    public MapBattleDefinitions() {
	this.battlers = new BattleCharacter[MapBattleDefinitions.MAX_BATTLERS];
	this.aiContexts = new MapAIContext[MapBattleDefinitions.MAX_BATTLERS];
	this.battlerCount = 0;
    }

    public boolean addBattler(final BattleCharacter battler) {
	if (this.battlerCount < MapBattleDefinitions.MAX_BATTLERS) {
	    this.battlers[this.battlerCount] = battler;
	    this.battlerCount++;
	    return true;
	}
	return false;
    }

    public int findBattler(final String name) {
	return this.findBattler(name, 0, this.battlers.length);
    }

    private int findBattler(final String name, final int start, final int limit) {
	for (var x = start; x < limit; x++) {
	    if (this.battlers[x] != null && this.battlers[x].getName().equals(name)) {
		return x;
	    }
	}
	return -1;
    }

    public int findFirstBattlerOnTeam(final int teamID) {
	return this.findFirstBattlerOnTeam(teamID, 0, this.battlers.length);
    }

    private int findFirstBattlerOnTeam(final int teamID, final int start, final int limit) {
	for (var x = start; x < limit; x++) {
	    if (this.battlers[x] != null && this.battlers[x].getTeamID() == teamID) {
		return x;
	    }
	}
	return -1;
    }

    public BattleCharacter getActiveCharacter() {
	return this.activeCharacter;
    }

    public Dungeon getBattleDungeon() {
	return this.battleMap;
    }

    public MapAIContext[] getBattlerAIContexts() {
	return this.aiContexts;
    }

    public BattleCharacter[] getBattlers() {
	return this.battlers;
    }

    public void resetBattlers() {
	for (final BattleCharacter battler : this.battlers) {
	    if (battler != null && battler.getTemplate().isAlive()) {
		battler.activate();
		battler.resetAP();
		battler.resetAttacks();
		battler.resetSpells();
		battler.resetLocation();
	    }
	}
    }

    public void roundResetBattlers() {
	for (final BattleCharacter battler : this.battlers) {
	    if (battler != null && battler.getTemplate().isAlive()) {
		battler.resetAP();
		battler.resetAttacks();
		battler.resetSpells();
	    }
	}
    }

    public void setActiveCharacter(final BattleCharacter bc) {
	this.activeCharacter = bc;
    }

    public void setBattleDungeon(final Dungeon bMap) {
	this.battleMap = bMap;
    }
}
