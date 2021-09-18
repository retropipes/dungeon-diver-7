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

public class DisruptedHotWall extends AbstractDisruptedObject {
	// Fields
	private int disruptionLeft;
	private static final int DISRUPTION_START = 20;

	// Constructors
	public DisruptedHotWall() {
		super();
		this.type.set(TypeConstants.TYPE_PLAIN_WALL);
		this.disruptionLeft = DisruptedHotWall.DISRUPTION_START;
		this.activateTimer(1);
		this.setMaterial(MaterialConstants.MATERIAL_FIRE);
	}

	DisruptedHotWall(final int disruption) {
		super();
		this.type.set(TypeConstants.TYPE_PLAIN_WALL);
		this.disruptionLeft = disruption;
		this.activateTimer(1);
		this.setMaterial(MaterialConstants.MATERIAL_FIRE);
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		if (laserType == ArrowTypeConstants.LASER_TYPE_STUNNER) {
			// Cool off disrupted hot wall
			SoundLoader.playSound(SoundConstants.SOUND_COOL_OFF);
			DungeonDiver7.getApplication().getGameManager().morph(new DisruptedWall(this.disruptionLeft), locX, locY, locZ,
					this.getLayer());
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
			DungeonDiver7.getApplication().getGameManager().morph(new HotWall(), locX, locY, z, this.getLayer());
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
		case MaterialConstants.MATERIAL_ICE:
			return new DisruptedWall(this.disruptionLeft);
		default:
			return this;
		}
	}
}