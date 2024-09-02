/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.RangeTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class CrystalBlock extends AbstractReactionWall {
    // Constructors
    public CrystalBlock() {
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var icb = new IcyCrystalBlock();
	    icb.setPreviousStateObject(this);
	    yield icb;
	}
	case Material.FIRE -> new HotCrystalBlock();
	default -> this;
	};
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getIdValue() {
	return 10;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	return switch (laserType) {
	case ShotTypes.MISSILE -> {
	    // Destroy crystal block
	    SoundLoader.playSound(Sounds.KABOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	case ShotTypes.BLUE -> /* Reflect laser */ DirectionResolver.resolveInvert(dirX, dirY);
	case ShotTypes.DISRUPTOR -> {
	    // Disrupt crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedCrystalBlock(), locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	default -> /* Pass laser through */ DirectionResolver.resolve(dirX, dirY);
	};
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	return DirectionResolver.resolve(dirX, dirY);
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (RangeTypes.getMaterialForRangeType(rangeType) == Material.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Material.FIRE) {
	    // Heat up crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Material.ICE) {
	    // Freeze crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Material.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	}
	return true;
    }
}