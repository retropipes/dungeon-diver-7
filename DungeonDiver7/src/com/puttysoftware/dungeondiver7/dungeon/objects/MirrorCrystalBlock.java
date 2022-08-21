/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.RangeTypes;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class MirrorCrystalBlock extends AbstractReactionWall {
    // Constructors
    public MirrorCrystalBlock() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
    }

    @Override
    public Directions laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy mirror crystal block
	    SoundLoader.playSound(SoundConstants.BOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else if (laserType == ShotTypes.BLUE) {
	    // Pass laser through
	    return DirectionResolver.resolve(dirX, dirY);
	} else if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt mirror crystal block
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedMirrorCrystalBlock(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else {
	    // Reflect laser
	    return DirectionResolver.resolveInvert(dirX, dirY);
	}
    }

    @Override
    public Directions laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	return DirectionResolver.resolve(dirX, dirY);
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (rangeType == RangeTypes.BOMB
		|| RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy mirror crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE) {
	    // Heat up mirror crystal block
	    SoundLoader.playSound(SoundConstants.MELT);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE) {
	    // Freeze mirror crystal block
	    SoundLoader.playSound(SoundConstants.FROZEN);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else {
	    // Do nothing
	    return true;
	}
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getBaseID() {
	return 26;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.ICE:
	    final IcyCrystalBlock icb = new IcyCrystalBlock();
	    icb.setPreviousState(this);
	    return icb;
	case Materials.FIRE:
	    return new HotCrystalBlock();
	default:
	    return this;
	}
    }
}