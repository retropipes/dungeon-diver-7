/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractPassThroughObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class OpenDoor extends AbstractPassThroughObject {
    // Constructors
    public OpenDoor() {
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.OPEN_DOOR;
    }

    @Override
    public String getDescription() {
	return "Open Doors can be closed by interacting with them.";
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
    public void interactAction() {
	SoundLoader.playSound(Sounds.DOOR_CLOSES);
	final var glm = DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new ClosedDoor());
	glm.redrawDungeon();
    }
}
