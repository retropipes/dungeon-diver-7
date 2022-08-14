/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovingObject;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.RandomGenerationRule;
import com.puttysoftware.randomrange.RandomRange;

public class MonsterTile extends AbstractMovingObject {
    // Constructors
    public MonsterTile() {
	super(false);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, int dirZ) {
	if (DungeonDiver7.getStuffBag().getMode() != StuffBag.STATUS_BATTLE) {
	    DungeonDiver7.getStuffBag().getBattle().doBattle();
	    DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().postBattle(this, dirX, dirY, true);
	}
    }

    @Override
    public void timerExpiredAction(final int dirX, final int dirY) {
	// Move the monster
	final RandomRange r = new RandomRange(0, 7);
	final Direction move = Direction.fromInternalValue(r.generate());
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().updateMonsterPosition(move, dirX, dirY, this, 0);
	this.activateTimer(1);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.NONE;
    }

    @Override
    public String getName() {
	return "Monster";
    }

    @Override
    public String getPluralName() {
	return "Monsters";
    }

    @Override
    public String getDescription() {
	return "Monsters are dangerous. Encountering one starts a battle.";
    }

    @Override
    public boolean shouldGenerateObject(final AbstractDungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return super.shouldGenerateObject(dungeon, row, col, level, layer);
	}
    }

    @Override
    public int getMinimumRequiredQuantity(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return (int) Math.pow(dungeon.getRows() * dungeon.getColumns(), 1.0 / 2.2);
	}
    }

    @Override
    public int getMaximumRequiredQuantity(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return (int) Math.pow(dungeon.getRows() * dungeon.getColumns(), 1.0 / 1.8);
	}
    }

    @Override
    public boolean isRequired(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return true;
	}
    }
}
