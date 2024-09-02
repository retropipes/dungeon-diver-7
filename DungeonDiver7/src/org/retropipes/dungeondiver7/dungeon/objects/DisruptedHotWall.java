/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class DisruptedHotWall extends AbstractDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedHotWall() {
	this.disruptionLeft = DisruptedHotWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Material.FIRE);
    }

    DisruptedHotWall(final int disruption) {
	this.disruptionLeft = disruption;
	this.activateTimer(1);
	this.setMaterial(Material.FIRE);
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> new DisruptedWall(this.disruptionLeft);
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 59;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.STUNNER) {
	    // Cool off disrupted hot wall
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedWall(this.disruptionLeft), locX, locY, locZ, this.getLayer());
	    return Direction.NONE;
	}
	// Stop laser
	return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new HotWall(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}