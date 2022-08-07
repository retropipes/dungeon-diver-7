/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractTeleport extends AbstractDungeonObject {
    // Constructors
    protected AbstractTeleport() {
	super(false, true, false);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_TELEPORT);
    }
}
