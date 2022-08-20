/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.RangeTypes;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class IcyCrystalBlock extends AbstractReactionWall {
    // Constructors
    public IcyCrystalBlock() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.ICE);
    }

    @Override
    public Directions laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy icy crystal block
	    SoundLoader.playSound(SoundConstants.BOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt icy crystal block
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    final DisruptedIcyCrystalBlock dicb = new DisruptedIcyCrystalBlock();
	    if (this.hasPreviousState()) {
		dicb.setPreviousState(this.getPreviousState());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(dicb, locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else {
	    // Stop laser
	    SoundLoader.playSound(SoundConstants.LASER_DIE);
	    return Directions.NONE;
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy icy crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE) {
	    // Heat up crystal block
	    SoundLoader.playSound(SoundConstants.MELT);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE) {
	    // Do nothing
	    return true;
	} else {
	    // Do nothing
	    return true;
	}
    }

    @Override
    public final int getBaseID() {
	return 127;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    } else {
		return new CrystalBlock();
	    }
	default:
	    return this;
	}
    }
}