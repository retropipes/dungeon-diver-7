/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.Dungeon;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractMovingObject;
import com.puttysoftware.dungeondiver7.integration1.dungeon.utility.RandomGenerationRule;
import com.puttysoftware.dungeondiver7.integration1.loader.ObjectImageConstants;

public class BossMonsterTile extends AbstractMovingObject {
    // Constructors
    public BossMonsterTile() {
	super(false);
	this.setSavedObject(new Empty());
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	if (Integration1.getApplication().getMode() != Application.STATUS_BATTLE) {
	    Integration1.getApplication().getBattle().doBossBattle();
	}
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.BOSS;
    }

    @Override
    public String getName() {
	return "Boss Monster";
    }

    @Override
    public String getPluralName() {
	return "Boss Monsters";
    }

    @Override
    public String getDescription() {
	return "Boss Monsters are very dangerous. Encountering one starts a boss battle.";
    }

    @Override
    public boolean shouldGenerateObject(final Dungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return super.shouldGenerateObject(dungeon, row, col, level, layer);
	}
    }

    @Override
    public int getMinimumRequiredQuantity(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return 1;
	}
    }

    @Override
    public int getMaximumRequiredQuantity(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	} else {
	    return 1;
	}
    }

    @Override
    public boolean isRequired(final Dungeon dungeon) {
	if (dungeon.getActiveLevel() == Dungeon.getMaxLevels() - 1) {
	    return false;
	} else {
	    return true;
	}
    }
}
