/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.utility.TypeConstants;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.integration1.loader.ObjectImageConstants;

public class Darkness extends AbstractPassThroughObject {
    // Constructors
    public Darkness() {
	super();
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.DARKNESS;
    }

    @Override
    public String getName() {
	return "Darkness";
    }

    @Override
    public String getPluralName() {
	return "Squares of Darkness";
    }

    @Override
    public String getDescription() {
	return "Squares of Darkness are what fills areas that cannot be seen.";
    }

    @Override
    protected void setTypes() {
	this.type.set(TypeConstants.TYPE_PASS_THROUGH);
	this.type.set(TypeConstants.TYPE_EMPTY_SPACE);
    }
}