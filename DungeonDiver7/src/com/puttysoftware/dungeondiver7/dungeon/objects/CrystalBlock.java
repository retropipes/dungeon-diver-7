/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.RangeTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class CrystalBlock extends AbstractReactionWall {
    // Constructors
    public CrystalBlock() {
	super();
	this.type.set(TypeConstants.TYPE_PLAIN_WALL);
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Destroy crystal block
	    SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_BLUE) {
	    // Reflect laser
	    return DirectionResolver.resolveRelativeDirectionInvert(dirX, dirY);
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_DISRUPTOR) {
	    // Disrupt crystal block
	    SoundLoader.playSound(SoundConstants.SOUND_DISRUPTED);
	    DungeonDiver7.getApplication().getGameManager().morph(new DisruptedCrystalBlock(), locX, locY, locZ,
		    this.getLayer());
	    return Direction.NONE;
	} else {
	    // Pass laser through
	    return DirectionResolver.resolveRelativeDirection(dirX, dirY);
	}
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	return DirectionResolver.resolveRelativeDirection(dirX, dirY);
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_METALLIC) {
	    // Destroy crystal block
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_FIRE) {
	    // Heat up crystal block
	    SoundLoader.playSound(SoundConstants.SOUND_MELT);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_ICE) {
	    // Freeze crystal block
	    SoundLoader.playSound(SoundConstants.SOUND_FROZEN);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_ICE), locX + dirX, locY + dirY, locZ,
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
    public final int getStringBaseID() {
	return 10;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_ICE:
	    final IcyCrystalBlock icb = new IcyCrystalBlock();
	    icb.setPreviousState(this);
	    return icb;
	case MaterialConstants.MATERIAL_FIRE:
	    return new HotCrystalBlock();
	default:
	    return this;
	}
    }
}