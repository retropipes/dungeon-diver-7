/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovingObject;
import org.retropipes.dungeondiver7.utility.RandomGenerationRule;

public class FinalBossMonsterTile extends AbstractMovingObject {
    // Constructors
    public FinalBossMonsterTile() {
	super(false);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.FINAL_BOSS;
    }

    @Override
    public int getMaximumRequiredQuantity(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() != Dungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	}
	return 1;
    }

    @Override
    public int getMinimumRequiredQuantity(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() != Dungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	}
	return 1;
    }

    @Override
    public boolean isRequired(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() != Dungeon.getMaxLevels() - 1) {
	    return false;
	}
	return true;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	if (DungeonDiver7.getStuffBag().getMode() != StuffBag.STATUS_BATTLE) {
	    DungeonDiver7.getStuffBag().getBattle().doFinalBossBattle();
	}
    }

    @Override
    public boolean shouldGenerateObject(final Dungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() != Dungeon.getMaxLevels() - 1) {
	    return false;
	}
	return super.shouldGenerateObject(dungeon, row, col, level, layer);
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	// Move the monster
	final var dungeon = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	final var move = dungeon.computeFinalBossMoveDirection(locX, locY, 0, 0);
	dungeon.updateMonsterPosition(move, locX, locY, this, 0);
	this.activateTimer(1);
    }
}
