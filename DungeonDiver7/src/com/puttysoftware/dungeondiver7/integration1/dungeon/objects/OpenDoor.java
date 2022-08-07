/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractPassThroughObject;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;

public class OpenDoor extends AbstractPassThroughObject {
    // Constructors
    public OpenDoor() {
	super();
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.OPEN_DOOR;
    }

    // Scriptability
    @Override
    public String getName() {
	return "Open Door";
    }

    @Override
    public String getPluralName() {
	return "Open Doors";
    }

    @Override
    public String getDescription() {
	return "Open Doors can be closed by interacting with them.";
    }

    @Override
    public void interactAction() {
	SoundLoader.playSound(SoundConstants.DOOR_CLOSES);
	final GameLogic glm = Integration1.getApplication().getGameLogic();
	GameLogic.morph(new ClosedDoor());
	glm.redrawDungeon();
    }
}
