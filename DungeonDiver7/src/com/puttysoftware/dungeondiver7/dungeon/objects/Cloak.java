/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractAttribute;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class Cloak extends AbstractAttribute {
    // Constructors
    public Cloak() {
	super();
	this.type.set(TypeConstants.TYPE_CLOAK);
    }

    @Override
    public final int getStringBaseID() {
	return 135;
    }

    @Override
    public AbstractDungeonObject attributeGameRenderHook() {
	return new Empty();
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.SOUND_DISCOVER);
	return Direction.NONE;
    }

    @Override
    public void postMoveAction(final int locX, final int locY, final int locZ) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.SOUND_DISCOVER);
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final Application app = DungeonDiver7.getApplication();
	app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.SOUND_DISCOVER);
    }
}