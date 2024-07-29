/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class Bricks extends AbstractReactionWall {
    // Constructors
    public Bricks() {
	this.setMaterial(Materials.PLASTIC);
    }

    @Override
    public final int getBaseID() {
	return 8;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	if (laserType == ShotTypes.POWER) {
	    // Laser keeps going
	    return DirectionResolver.resolve(dirX, dirY);
	}
	// Laser stops
	return Direction.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	return true;
    }
}