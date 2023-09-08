/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractAttribute;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public class Crumbling extends AbstractAttribute {
    // Constructors
    public Crumbling() {
    }

    @Override
    public final int getBaseID() {
	return 132;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	app.getGameLogic();
	// Destroy whatever we were attached to
	GameLogic.morph(new Empty(), locX, locY, locZ, DungeonConstants.LAYER_LOWER_OBJECTS);
	SoundLoader.playSound(Sounds.CRACK);
	return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	app.getGameLogic();
	// Destroy whatever we were attached to
	GameLogic.morph(new Empty(), locX, locY, locZ, DungeonConstants.LAYER_LOWER_OBJECTS);
	SoundLoader.playSound(Sounds.CRACK);
    }
}