/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDisruptedObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class DisruptedIcyWall extends AbstractDisruptedObject {
	// Fields
	private int disruptionLeft;
	private static final int DISRUPTION_START = 20;

	// Constructors
	public DisruptedIcyWall() {
		super();
		this.type.set(TypeConstants.TYPE_PLAIN_WALL);
		this.disruptionLeft = DisruptedIcyWall.DISRUPTION_START;
		this.activateTimer(1);
		this.setMaterial(MaterialConstants.MATERIAL_ICE);
	}

	DisruptedIcyWall(final int disruption) {
		super();
		this.type.set(TypeConstants.TYPE_PLAIN_WALL);
		this.disruptionLeft = disruption;
		this.activateTimer(1);
		this.setMaterial(MaterialConstants.MATERIAL_ICE);
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
			// Defrost icy wall
			SoundLoader.playSound(SoundConstants.SOUND_DEFROST);
			final DisruptedWall dw = new DisruptedWall();
			if (this.hasPreviousState()) {
				dw.setPreviousState(this.getPreviousState());
			}
			DungeonDiver7.getApplication().getGameManager().morph(dw, locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		} else {
			// Stop laser
			return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
		}
	}

	@Override
	public void timerExpiredAction(final int locX, final int locY) {
		this.disruptionLeft--;
		if (this.disruptionLeft == 0) {
			SoundLoader.playSound(SoundConstants.SOUND_DISRUPT_END);
			final int z = DungeonDiver7.getApplication().getGameManager().getPlayerManager().getPlayerLocationZ();
			final IcyWall iw = new IcyWall();
			if (this.hasPreviousState()) {
				iw.setPreviousState(this.getPreviousState());
			}
			DungeonDiver7.getApplication().getGameManager().morph(iw, locX, locY, z, this.getLayer());
		} else {
			this.activateTimer(1);
		}
	}

	@Override
	public final int getStringBaseID() {
		return 59;
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		switch (materialID) {
		case MaterialConstants.MATERIAL_FIRE:
			return new DisruptedWall(this.disruptionLeft);
		default:
			return this;
		}
	}
}