/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.RangeTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class HotCrystalBlock extends AbstractReactionWall {
    // Constructors
    public HotCrystalBlock() {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.FIRE);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> new CrystalBlock();
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 126;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy hot crystal block
	    SoundLoader.playSound(Sounds.KABOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt hot crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedHotCrystalBlock(), locX, locY, locZ, this.getLayer());
	} else {
	    // Stop laser
	}
	return Direction.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy hot crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE
		|| RangeTypes.getMaterialForRangeType(rangeType) != Materials.ICE) {
	} else {
	    // Freeze crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	}
	// Do nothing
	return true;
    }
}