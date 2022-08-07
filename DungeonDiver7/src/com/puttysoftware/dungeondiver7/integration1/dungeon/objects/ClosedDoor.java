/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.integration1.game.GameLogic;
import com.puttysoftware.dungeondiver7.integration1.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.integration1.loader.SoundManager;

public class ClosedDoor extends AbstractPassThroughObject {
    // Constructors
    public ClosedDoor() {
	super();
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.CLOSED_DOOR;
    }

    // Scriptability
    @Override
    public String getName() {
	return "Closed Door";
    }

    @Override
    public String getPluralName() {
	return "Closed Doors";
    }

    @Override
    public String getDescription() {
	return "Closed Doors open when stepped on.";
    }

    @Override
    public void interactAction() {
	SoundManager.playSound(SoundConstants.DOOR_OPENS);
	final GameLogic glm = Integration1.getApplication().getGameLogic();
	GameLogic.morph(new OpenDoor());
	glm.redrawDungeon();
    }
}
