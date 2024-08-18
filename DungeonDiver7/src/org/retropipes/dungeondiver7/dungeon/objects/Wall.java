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
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class Wall extends AbstractWall {
    // Constructors
    public Wall() {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> {
	    final var iw = new IcyWall();
	    iw.setPreviousState(this);
	    yield iw;
	}
	case Materials.FIRE -> new HotWall();
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 45;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	return switch (laserType) {
	case ShotTypes.DISRUPTOR -> {
	    // Disrupt wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedWall(), locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	case ShotTypes.MISSILE -> {
	    // Heat up wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new HotWall(), locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	case ShotTypes.STUNNER -> {
	    // Freeze wall
	    final var iw = new IcyWall();
	    iw.setPreviousState(this);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(iw, locX, locY, locZ, this.getLayer());
	    yield Direction.NONE;
	}
	default -> /* Stop laser */ super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	};
    }
}