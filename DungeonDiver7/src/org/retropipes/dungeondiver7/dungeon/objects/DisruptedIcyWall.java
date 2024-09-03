/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;

public class DisruptedIcyWall extends AbstractDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedIcyWall() {
	this.disruptionLeft = DisruptedIcyWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Material.ICE);
    }

    DisruptedIcyWall(final int disruption) {
	this.disruptionLeft = disruption;
	this.activateTimer(1);
	this.setMaterial(Material.ICE);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.FIRE -> new DisruptedWall(this.disruptionLeft);
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 59;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    final var iw = new IcyWall();
	    if (this.hasPreviousState()) {
		iw.setPreviousStateObject(this.getPreviousStateObject());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(iw, locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}