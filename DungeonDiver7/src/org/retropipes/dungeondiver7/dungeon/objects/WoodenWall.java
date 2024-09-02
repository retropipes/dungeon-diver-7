/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractWall;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class WoodenWall extends AbstractWall {
    // Constructors
    public WoodenWall() {
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.FIRE -> new Ground();
	case Material.ICE -> {
	    final var iw = new IcyWall();
	    iw.setPreviousStateObject(this);
	    yield iw;
	}
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 56;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	return switch (laserType) {
	case ShotTypes.DISRUPTOR -> {
	    // Disrupt wooden wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedWoodenWall(), locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	case ShotTypes.MISSILE -> {
	    // Destroy wooden wall
	    SoundLoader.playSound(Sounds.KABOOM);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	case ShotTypes.STUNNER -> {
	    // Freeze wooden wall
	    final var iw = new IcyWall();
	    iw.setPreviousStateObject(this);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(iw, locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	default -> /* Stop laser */ super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	};
    }
}