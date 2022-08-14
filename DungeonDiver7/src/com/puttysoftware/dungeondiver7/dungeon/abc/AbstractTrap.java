/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;
import com.puttysoftware.randomrange.RandomRange;

public abstract class AbstractTrap extends AbstractDungeonObject {
    // Fields
    private final int base;

    // Constructors
    protected AbstractTrap(final int baseName) {
	super(false, false);
	this.base = baseName;
	this.type.set(TypeConstants.TYPE_TRAP);
    }

    // Scriptability
    @Override
    public int getBaseID() {
	return this.base;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    public boolean shouldGenerateObject(final CurrentDungeon maze, final int row, final int col, final int level,
	    final int layer) {
	// Generate all traps at 25% rate
	final RandomRange reject = new RandomRange(1, 100);
	return reject.generate() < 25;
    }
}