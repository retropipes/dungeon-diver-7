/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class WoodenWall extends AbstractWall {
    // Constructors
    public WoodenWall() {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.WOODEN);
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	switch (laserType) {
	case ShotTypes.DISRUPTOR:
	    // Disrupt wooden wall
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedWoodenWall(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	case ShotTypes.MISSILE:
	    // Destroy wooden wall
	    SoundLoader.playSound(SoundConstants.BOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	case ShotTypes.STUNNER: {
	    // Freeze wooden wall
	    SoundLoader.playSound(SoundConstants.FROZEN);
	    final var iw = new IcyWall();
	    iw.setPreviousState(this);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(iw, locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	}
	default:
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public final int getBaseID() {
	return 56;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.FIRE:
	    return new Ground();
	case Materials.ICE:
	    final var iw = new IcyWall();
	    iw.setPreviousState(this);
	    return iw;
	default:
	    return this;
	}
    }
}