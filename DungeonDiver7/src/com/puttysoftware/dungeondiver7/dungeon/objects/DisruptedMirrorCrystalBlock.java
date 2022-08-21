/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractReactionDisruptedObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.RangeTypes;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class DisruptedMirrorCrystalBlock extends AbstractReactionDisruptedObject {
    // Fields
    private int disruptionLeft;
    private static final int DISRUPTION_START = 20;

    // Constructors
    public DisruptedMirrorCrystalBlock() {
	super();
	this.disruptionLeft = DisruptedMirrorCrystalBlock.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public Directions laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy disrupted mirror crystal block
	    SoundLoader.playSound(SoundConstants.BOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else if (laserType == ShotTypes.BLUE) {
	    // Pass laser through
	    return DirectionResolver.resolve(dirX, dirY);
	} else {
	    // Reflect laser
	    return DirectionResolver.resolveInvert(dirX, dirY);
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    SoundLoader.playSound(SoundConstants.DISRUPT_END);
	    final int z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new MirrorCrystalBlock(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
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
	    // Destroy disrupted mirror crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
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
	return 51;
    }
}