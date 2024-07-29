/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionDisruptedObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.RangeTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class DisruptedMirrorCrystalBlock extends AbstractReactionDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedMirrorCrystalBlock() {
	this.disruptionLeft = DisruptedMirrorCrystalBlock.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getBaseID() {
	return 51;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy disrupted mirror crystal block
	    SoundLoader.playSound(Sounds.KABOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	if (laserType == ShotTypes.BLUE) {
	    // Pass laser through
	    return DirectionResolver.resolve(dirX, dirY);
	}
	// Reflect laser
	return DirectionResolver.resolveInvert(dirX, dirY);
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	return DirectionResolver.resolve(dirX, dirY);
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (rangeType == RangeTypes.BOMB || RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy disrupted mirror crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	}
	// Do nothing
	return true;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    SoundLoader.playSound(Sounds.DISRUPT_END);
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new MirrorCrystalBlock(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}