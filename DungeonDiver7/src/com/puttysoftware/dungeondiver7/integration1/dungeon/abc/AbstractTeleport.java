/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import com.puttysoftware.dungeondiver7.integration1.dungeon.DungeonConstants;
import com.puttysoftware.dungeondiver7.integration1.dungeon.utility.TypeConstants;

public abstract class AbstractTeleport extends AbstractGameObject {
    // Constructors
    protected AbstractTeleport() {
	super(false, true, false);
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_OBJECT;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_TELEPORT);
    }
}
