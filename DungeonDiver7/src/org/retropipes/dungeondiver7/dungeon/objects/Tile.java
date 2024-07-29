/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;

public class Tile extends AbstractGround {
    // Constructors
    public Tile() {
    }

    @Override
    public final int getBaseID() {
	return ObjectImageConstants.TILE;
    }
}