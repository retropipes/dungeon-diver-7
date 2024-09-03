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

public class DisruptedWall extends AbstractDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedWall() {
	this.disruptionLeft = DisruptedWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Material.METALLIC);
    }

    DisruptedWall(final int disruption) {
	this.disruptionLeft = disruption;
	this.activateTimer(1);
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var diw = new DisruptedIcyWall(this.disruptionLeft);
	    diw.setPreviousStateObject(this);
	    yield diw;
	}
	case Material.FIRE -> new DisruptedHotWall(this.disruptionLeft);
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 52;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new Wall(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}