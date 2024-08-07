/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractField;
import org.retropipes.dungeondiver7.game.GameLogic;

public class PowerBolt extends AbstractField {
    // Constructors
    public PowerBolt() {
    }

    @Override
    public final int getBaseID() {
	return 139;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
	DungeonDiver7.getStuffBag().getGameLogic().setPowerfulPlayer();
    }
}