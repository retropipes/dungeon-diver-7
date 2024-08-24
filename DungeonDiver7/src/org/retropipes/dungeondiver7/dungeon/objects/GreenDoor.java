/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDoor;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.PartyInventory;

public class GreenDoor extends AbstractDoor {
    // Constructors
    public GreenDoor() {
	super(new GreenKey());
    }

    @Override
    public final int getIdValue() {
	return 16;
    }

    // Scriptability
    @Override
    public boolean isConditionallySolid() {
	return PartyInventory.getGreenKeysLeft() < 1;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(Sounds.UNLOCK);
	PartyInventory.useGreenKey();
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}