/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractTeleport;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;

public class StairsDown extends AbstractTeleport {
    // Constructors
    public StairsDown() {
	super();
    }

    @Override
    public int getDestinationFloor() {
	final Application app = DungeonDiver7.getApplication();
	return app.getGameManager().getPlayerManager().getPlayerLocationZ() - 1;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().updatePositionAbsoluteNoEvents(this.getDestinationFloor());
	SoundLoader.playSound(SoundConstants.SOUND_DOWN);
    }

    @Override
    public final int getStringBaseID() {
	return 32;
    }
}
