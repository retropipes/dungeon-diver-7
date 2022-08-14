/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDoor;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.PartyInventory;

public class GreenDoor extends AbstractDoor {
    // Constructors
    public GreenDoor() {
	super(new GreenKey());
    }

    // Scriptability
    @Override
    public boolean isConditionallySolid() {
	return PartyInventory.getGreenKeysLeft() < 1;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(SoundConstants.UNLOCK);
	PartyInventory.useGreenKey();
	DungeonDiver7.getApplication().getGameLogic();
	GameLogic.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }

    @Override
    public final int getBaseID() {
	return 16;
    }
}