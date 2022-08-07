/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractMarker extends AbstractDungeonObject {
    // Constructors
    protected AbstractMarker() {
	super(false, false);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, int dirZ) {
	SoundLoader.playSound(SoundConstants.WALK);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_VIRTUAL_CHARACTER;
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