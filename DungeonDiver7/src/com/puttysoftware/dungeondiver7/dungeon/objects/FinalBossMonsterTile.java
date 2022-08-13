/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovingObject;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
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
	if (Integration1.getApplication().getMode() != Application.STATUS_BATTLE) {
	    Integration1.getApplication().getBattle().doFinalBossBattle();
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	// Move the monster
	CurrentDungeon dungeon = Integration1.getApplication().getDungeonManager().getDungeon();
	final Direction move = dungeon.computeFinalBossMoveDirection(locX, locY, 0);
	dungeon.updateMonsterPosition(move, locX, locY, this);
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
    public boolean shouldGenerateObject(final CurrentDungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() != CurrentDungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return super.shouldGenerateObject(dungeon, row, col, level, layer);
	}
    }

    @Override
    public int getMinimumRequiredQuantity(final CurrentDungeon dungeon) {
	if (dungeon.getActiveLevel() != CurrentDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return 1;
	}
    }

    @Override
    public int getMaximumRequiredQuantity(final CurrentDungeon dungeon) {
	if (dungeon.getActiveLevel() != CurrentDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return 1;
	}
    }

    @Override
    public boolean isRequired(final CurrentDungeon dungeon) {
	if (dungeon.getActiveLevel() != CurrentDungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return true;
	}
    }
}
