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
import com.puttysoftware.dungeondiver7.utility.RandomGenerationRule;

public class BossMonsterTile extends AbstractMovingObject {
    // Constructors
    public BossMonsterTile() {
	super(false);
	this.setSavedObject(new Empty());
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	if (DungeonDiver7.getStuffBag().getMode() != StuffBag.STATUS_BATTLE) {
	    DungeonDiver7.getStuffBag().getBattle().doBossBattle();
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
    public boolean shouldGenerateObject(final AbstractDungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return false;
	}
	return super.shouldGenerateObject(dungeon, row, col, level, layer);
    }

    @Override
    public int getMinimumRequiredQuantity(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	}
	return 1;
    }

    @Override
    public int getMaximumRequiredQuantity(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return RandomGenerationRule.NO_LIMIT;
	}
	return 1;
    }

    @Override
    public boolean isRequired(final AbstractDungeon dungeon) {
	if (dungeon.getActiveLevel() == AbstractDungeon.getMaxLevels() - 1) {
	    return false;
	}
	return true;
    }
}
