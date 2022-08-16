/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractAttribute;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class Cloak extends AbstractAttribute {
    // Constructors
    public Cloak() {
	super();
	this.type.set(TypeConstants.TYPE_CLOAK);
    }

    @Override
    public final int getBaseID() {
	return 135;
    }

    @Override
    public AbstractDungeonObject attributeGameRenderHook() {
	return new Empty();
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.DISCOVER);
	return Direction.NONE;
    }

    @Override
    public void postMoveAction(final int locX, final int locY, final int locZ) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.DISCOVER);
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(SoundConstants.DISCOVER);
    }
}