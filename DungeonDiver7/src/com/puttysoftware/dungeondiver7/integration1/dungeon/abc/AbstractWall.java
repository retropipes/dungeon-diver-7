/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractWall extends AbstractDungeonObject {
    // Constructors
    protected AbstractWall() {
	super(true, true);
	this.type.set(TypeConstants.TYPE_WALL);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, int dirZ) {
	// Do nothing
    }

    @Override
    public void moveFailedAction(final int dirX, final int dirY, int dirZ) {
	Integration1.getApplication().showMessage("Can't go that way");
	// Play move failed sound, if it's enabled
	SoundLoader.playSound(SoundConstants.FAILED);
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
}