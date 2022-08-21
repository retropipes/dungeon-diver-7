/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class DisruptedHotWall extends AbstractDisruptedObject {
    // Fields
    private int disruptionLeft;
    private static final int DISRUPTION_START = 20;

    // Constructors
    public DisruptedHotWall() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.disruptionLeft = DisruptedHotWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Materials.FIRE);
    }

    DisruptedHotWall(final int disruption) {
	super();
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.disruptionLeft = disruption;
	this.activateTimer(1);
	this.setMaterial(Materials.FIRE);
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.STUNNER) {
	    // Cool off disrupted hot wall
	    SoundLoader.playSound(SoundConstants.COOL_OFF);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedWall(this.disruptionLeft), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    SoundLoader.playSound(SoundConstants.DISRUPT_END);
	    final int z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new HotWall(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }

    @Override
    public final int getBaseID() {
	return 59;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.ICE:
	    return new DisruptedWall(this.disruptionLeft);
	default:
	    return this;
	}
    }
}