/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.BagOStuff;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTeleport;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;

public class StairsUp extends AbstractTeleport {
    // Constructors
    public StairsUp() {
	super();
    }

    @Override
    public int getDestinationFloor() {
	final BagOStuff app = DungeonDiver7.getApplication();
	return app.getGameLogic().getPlayerManager().getPlayerLocationZ() + 1;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final BagOStuff app = DungeonDiver7.getApplication();
	app.getGameLogic().updatePositionAbsoluteNoEvents(this.getDestinationFloor());
	SoundLoader.playSound(SoundConstants.UP);
    }

    @Override
    public final int getBaseID() {
	return 33;
    }
}
