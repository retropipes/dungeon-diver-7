/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractWall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class HotWall extends AbstractWall {
    // Constructors
    public HotWall() {
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.FIRE);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> new Wall();
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 60;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt hot wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedHotWall(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	if (laserType == ShotTypes.STUNNER) {
	    // Cool off hot wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Wall(), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	// Stop laser
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }
}