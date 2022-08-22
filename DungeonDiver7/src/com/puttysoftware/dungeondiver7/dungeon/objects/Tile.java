/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;

public class Tile extends AbstractGround {
    // Constructors
    public Tile() {
    }

    @Override
    public final int getBaseID() {
	return ObjectImageConstants.TILE;
    }

    @Override
    public String getName() {
	return "Tile";
    }

    @Override
    public String getPluralName() {
	return "Tiles";
    }

    @Override
    public String getDescription() {
	return "Tile is one of the many types of ground - unlike other types of ground, objects can be pushed and pulled over Tiles.";
    }
}