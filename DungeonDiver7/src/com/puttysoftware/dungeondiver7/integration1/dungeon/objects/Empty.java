/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class Empty extends AbstractPassThroughObject {
    // Constructors
    public Empty() {
	super();
	this.type.set(TypeConstants.TYPE_PASS_THROUGH);
	this.type.set(TypeConstants.TYPE_EMPTY_SPACE);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.EMPTY;
    }

    @Override
    public String getName() {
	return "Empty";
    }

    @Override
    public String getPluralName() {
	return "Squares of Emptiness";
    }

    @Override
    public String getDescription() {
	return "Squares of Emptiness are what fills areas that aren't occupied by other objects.";
    }
}