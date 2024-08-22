/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovingObject;
import org.retropipes.dungeondiver7.utility.RandomGenerationRule;

public class MonsterTile extends AbstractMovingObject {
    // Constructors
    public MonsterTile() {
	super(false);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
    }

    @Override
    public int getId() {
	return ObjectImageConstants.NONE;
    }

    @Override
    public int getMaximumRequiredQuantity(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	}
	return (int) Math.pow(dungeon.getRows() * dungeon.getColumns(), 1.0 / 1.8);
    }

    @Override
    public int getMinimumRequiredQuantity(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	}
	return (int) Math.pow(dungeon.getRows() * dungeon.getColumns(), 1.0 / 2.2);
    }

    @Override
    public boolean isRequired(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return false;
	}
	return true;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	if (DungeonDiver7.getStuffBag().getMode() != StuffBag.STATUS_BATTLE) {
	    DungeonDiver7.getStuffBag().getBattle().doBattle();
	    DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().postBattle(this, dirX, dirY, true);
	}
    }

    @Override
    public boolean shouldGenerateObject(final Dungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return false;
	}
	return super.shouldGenerateObject(dungeon, row, col, level, layer);
    }

    @Override
    public void timerExpiredAction(final int dirX, final int dirY) {
	// Move the monster
	final var r = new RandomRange(0, 7);
	final var move = Direction.values()[r.generate()];
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().updateMonsterPosition(move, dirX, dirY, this, 0);
	this.activateTimer(1);
    }
}
