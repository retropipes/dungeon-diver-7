/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.ai;

import java.awt.Point;

import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.BattleCharacter;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public class AIContext {
    private static final int MINIMUM_RADIUS = 1;
    private static final int MAXIMUM_RADIUS = 16;
    private static final int NOTHING_THERE = -1;
    private static final int AP_COST = 1;

    // Static method
    public static int getAPCost() {
	return AIContext.AP_COST;
    }

    private final BattleCharacter battleCharacter;
    private final Creature creature;
    private final int myTeam;
    private final int[][] creatureLocations;

    // Constructor
    public AIContext(final BattleCharacter bc, final int rows, final int columns) {
	this.battleCharacter = bc;
	this.creature = bc.getTemplate();
	this.myTeam = bc.getTeamID();
	this.creatureLocations = new int[rows][columns];
    }

    public BattleCharacter getCharacter() {
	return this.battleCharacter;
    }

    public Creature getCreature() {
	return this.creature;
    }

    public Point isEnemyNearby() {
	return this.isEnemyNearby(1, 1);
    }

    public Point isEnemyNearby(final int minRadius, final int maxRadius) {
	var fMinR = minRadius;
	var fMaxR = maxRadius;
	if (fMaxR > AIContext.MAXIMUM_RADIUS) {
	    fMaxR = AIContext.MAXIMUM_RADIUS;
	}
	if (fMaxR < AIContext.MINIMUM_RADIUS) {
	    fMaxR = AIContext.MINIMUM_RADIUS;
	}
	if (fMinR > AIContext.MAXIMUM_RADIUS) {
	    fMinR = AIContext.MAXIMUM_RADIUS;
	}
	if (fMinR < AIContext.MINIMUM_RADIUS) {
	    fMinR = AIContext.MINIMUM_RADIUS;
	}
	final var x = this.battleCharacter.getX();
	final var y = this.battleCharacter.getY();
	int u, v;
	for (u = x - fMaxR; u <= x + fMaxR; u++) {
	    for (v = y - fMaxR; v <= y + fMaxR; v++) {
		if (Math.abs(u - x) < fMinR && Math.abs(v - y) < fMinR) {
		    continue;
		}
		try {
		    if (this.creatureLocations[u][v] != -1 && this.creatureLocations[u][v] != this.myTeam) {
			return new Point(u - x, v - y);
		    }
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
	    }
	}
	return null;
    }

    public Point runAway() {
	final var fMinR = AIContext.MAXIMUM_RADIUS;
	final var fMaxR = AIContext.MAXIMUM_RADIUS;
	final var x = this.battleCharacter.getX();
	final var y = this.battleCharacter.getY();
	int u, v;
	for (u = x - fMaxR; u <= x + fMaxR; u++) {
	    for (v = y - fMaxR; v <= y + fMaxR; v++) {
		if (Math.abs(u - x) < fMinR && Math.abs(v - y) < fMinR) {
		    continue;
		}
		try {
		    if (this.creatureLocations[u][v] != -1 && this.creatureLocations[u][v] != this.myTeam) {
			return new Point(u + x, v + y);
		    }
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
	    }
	}
	return null;
    }

    public void updateContext(final Dungeon arena) {
	for (var x = 0; x < this.creatureLocations.length; x++) {
	    for (var y = 0; y < this.creatureLocations[x].length; y++) {
		final var obj = arena.getCell(x, y, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
		if (obj instanceof final BattleCharacter bc) {
		    this.creatureLocations[x][y] = bc.getTeamID();
		} else {
		    this.creatureLocations[x][y] = AIContext.NOTHING_THERE;
		}
	    }
	}
    }
}
