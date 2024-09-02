/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.RangeTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class IcyCrystalBlock extends AbstractReactionWall {
    // Constructors
    public IcyCrystalBlock() {
	this.setMaterial(Material.ICE);
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case Material.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousStateObject();
	    }
	    return new CrystalBlock();
	default:
	    return this;
	}
    }

    @Override
    public final int getIdValue() {
	return 127;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.MISSILE) {
	    // Destroy icy crystal block
	    SoundLoader.playSound(Sounds.KABOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt icy crystal block
	    final var dicb = new DisruptedIcyCrystalBlock();
	    if (this.hasPreviousState()) {
		dicb.setPreviousStateObject(this.getPreviousStateObject());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(dicb, locX, locY, locZ, this.getLayer());
	} else {
	    // Stop laser
	}
	return Direction.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (RangeTypes.getMaterialForRangeType(rangeType) == Material.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy icy crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Material.FIRE) {
	    // Heat up crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Material.ICE) {
	}
	return true;
    }
}