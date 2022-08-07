/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.DungeonConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundManager;

public abstract class AbstractWall extends AbstractGameObject {
    // Constructors
    protected AbstractWall() {
	super(true, true);
    }

    @Override
    public void postMoveAction(final boolean ie, final int dirX, final int dirY) {
	// Do nothing
    }

    @Override
    public void moveFailedAction(final boolean ie, final int dirX, final int dirY) {
	Integration1.getApplication().showMessage("Can't go that way");
	// Play move failed sound, if it's enabled
	SoundManager.playSound(SoundConstants.FAILED);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_OBJECT;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_WALL);
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractGameObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}