/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractAttribute;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.Direction;

public class Cracked extends AbstractAttribute {
    // Constructors
    public Cracked() {
	super();
    }

    @Override
    public final int getStringBaseID() {
	return 131;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Damaged(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.SOUND_CRACK);
	return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Damaged(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.SOUND_CRACK);
    }
}