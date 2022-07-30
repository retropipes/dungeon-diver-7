/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractReactionDisruptedObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.RangeTypeConstants;

public class DisruptedIcyCrystalBlock extends AbstractReactionDisruptedObject {
    // Fields
    private int disruptionLeft;
    private static final int DISRUPTION_START = 20;

    // Constructors
    public DisruptedIcyCrystalBlock() {
	super();
	this.disruptionLeft = DisruptedIcyCrystalBlock.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(MaterialConstants.MATERIAL_ICE);
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Destroy disrupted icy crystal block
	    SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	} else if (laserType == ArrowTypeConstants.LASER_TYPE_BLUE) {
	    // Reflect laser
	    return DirectionResolver.resolveRelativeDirectionInvert(dirX, dirY);
	} else {
	    // Pass laser through
	    return DirectionResolver.resolveRelativeDirection(dirX, dirY);
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    SoundLoader.playSound(SoundConstants.SOUND_DISRUPT_END);
	    final int z = DungeonDiver7.getApplication().getGameManager().getPlayerManager().getPlayerLocationZ();
	    final IcyCrystalBlock icb = new IcyCrystalBlock();
	    if (this.hasPreviousState()) {
		icb.setPreviousState(this.getPreviousState());
	    }
	    DungeonDiver7.getApplication().getGameManager().morph(icb, locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
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
	if (rangeType == RangeTypeConstants.RANGE_TYPE_BOMB
		|| RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_METALLIC) {
	    // Destroy disrupted icy crystal block
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX + dirX, locY + dirY, locZ,
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
	return 129;
    }
}