/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.integration1.loader.ObjectImageConstants;

public class Wall extends AbstractWall {
    // Constructors
    public Wall() {
	super();
    }

    @Override
    public String getName() {
	return "Wall";
    }

    @Override
    public String getPluralName() {
	return "Walls";
    }

    @Override
    public String getDescription() {
	return "Walls are impassable - you'll need to go around them.";
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.WALL;
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_PLAIN_WALL);
	this.type.set(TypeConstants.TYPE_WALL);
    }
}