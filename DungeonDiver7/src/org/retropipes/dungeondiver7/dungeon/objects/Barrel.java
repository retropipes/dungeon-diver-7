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
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class Barrel extends AbstractReactionWall {
    // Constructors
    public Barrel() {
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public final int getIdValue() {
	return 3;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	// Boom!
	DungeonDiver7.getStuffBag().getGameLogic();
	// Destroy barrel
	GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
	// Check for tank in range of explosion
	final var target = a.circularScanPlayer(locX, locY, locZ, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getStuffBag().getGameLogic().gameOver();
	}
	if (laserType == ShotTypes.POWER) {
	    // Laser keeps going
	    return DirectionResolver.resolve(dirX, dirY);
	}
	// Laser stops
	return Direction.NONE;
    }

    @Override
    public void pushCollideAction(final DungeonObject pushed, final int x, final int y, final int z) {
	// React to movable objects hitting barrels
	if (pushed.canMove()) {
	    this.laserEnteredAction(x, y, z, 0, 0, ShotTypes.GREEN, 1);
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	// Boom!
	// Check for tank in range of explosion
	final var target = a.circularScanPlayer(locX + dirX, locY + dirY, locZ, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getStuffBag().getGameLogic().gameOver();
	    return true;
	}
	DungeonDiver7.getStuffBag().getGameLogic();
	// Destroy barrel
	GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	return true;
    }
}