/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractAttribute;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.Direction;

public class Crumbling extends AbstractAttribute {
    // Constructors
    public Crumbling() {
	super();
    }

    @Override
    public final int getStringBaseID() {
	return 132;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	// Destroy whatever we were attached to
	app.getGameManager().morph(new Empty(), locX, locY, locZ, DungeonConstants.LAYER_LOWER_OBJECTS);
	SoundLoader.playSound(SoundConstants.SOUND_CRACK);
	return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	// Destroy whatever we were attached to
	app.getGameManager().morph(new Empty(), locX, locY, locZ, DungeonConstants.LAYER_LOWER_OBJECTS);
	SoundLoader.playSound(SoundConstants.SOUND_CRACK);
    }
}