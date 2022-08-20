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
import com.puttysoftware.dungeondiver7.utility.ShotTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class Wall extends AbstractWall {
    // Constructors
    public Wall() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt wall
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedWall(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else if (laserType == ShotTypes.MISSILE) {
	    // Heat up wall
	    SoundLoader.playSound(SoundConstants.MELT);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new HotWall(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else if (laserType == ShotTypes.STUNNER) {
	    // Freeze wall
	    SoundLoader.playSound(SoundConstants.FROZEN);
	    final IcyWall iw = new IcyWall();
	    iw.setPreviousState(this);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(iw, locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public final int getBaseID() {
	return 45;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.ICE:
	    final IcyWall iw = new IcyWall();
	    iw.setPreviousState(this);
	    return iw;
	case Materials.FIRE:
	    return new HotWall();
	default:
	    return this;
	}
    }
}