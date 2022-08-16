/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovingObject;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.RandomGenerationRule;

public class FinalBossMonsterTile extends AbstractMovingObject {
    // Constructors
    public FinalBossMonsterTile() {
	super(false);
	this.setSavedObject(new Empty());
	this.activateTimer(1);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, int dirZ) {
	if (DungeonDiver7.getStuffBag().getMode() != StuffBag.STATUS_BATTLE) {
	    DungeonDiver7.getStuffBag().getBattle().doFinalBossBattle();
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	// Move the monster
	AbstractDungeon dungeon = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	final Direction move = dungeon.computeFinalBossMoveDirection(locX, locY, 0, 0);
	dungeon.updateMonsterPosition(move, locX, locY, this, 0);
	this.activateTimer(1);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.FINAL_BOSS;
    }

    @Override
    public String getName() {
	return "Final Boss Monster";
    }

    @Override
    public String getPluralName() {
	return "Final Boss Monsters";
    }

    @Override
    public String getDescription() {
	return "Final Boss Monsters are extremely dangerous. Encountering one starts a final boss battle.";
    }

    @Override
    public boolean shouldGenerateObject(final AbstractDungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() != AbstractDungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return super.shouldGenerateObject(dungeon, row, col, level, layer);
	}
    }

    @Override
    public int getMinimumRequiredQuantity(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() != AbstractDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return 1;
	}
    }

    @Override
    public int getMaximumRequiredQuantity(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() != AbstractDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return 1;
	}
    }

    @Override
    public boolean isRequired(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() != AbstractDungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return true;
	}
    }
}
