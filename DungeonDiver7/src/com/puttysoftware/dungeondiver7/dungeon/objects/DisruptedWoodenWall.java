/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class DisruptedWoodenWall extends AbstractDisruptedObject {
	private static final int DISRUPTION_START = 20;
	// Fields
	private int disruptionLeft;

	// Constructors
	public DisruptedWoodenWall() {
		this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
		this.disruptionLeft = DisruptedWoodenWall.DISRUPTION_START;
		this.activateTimer(1);
		this.setMaterial(Materials.WOODEN);
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		return switch (materialID) {
		case Materials.FIRE -> new Ground();
		case Materials.ICE -> new DisruptedIcyWall();
		default -> this;
		};
	}

	@Override
	public final int getBaseID() {
		return 57;
	}

	@Override
	public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType, final int forceUnits) {
		if (laserType == ShotTypes.MISSILE) {
			// Destroy disrupted wooden wall
			SoundLoader.playSound(Sounds.BOOM);
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		}
		if (laserType == ShotTypes.STUNNER) {
			// Freeze disrupted wooden wall
			SoundLoader.playSound(Sounds.FROZEN);
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(new DisruptedIcyWall(this.disruptionLeft), locX, locY, locZ, this.getLayer());
			return Direction.NONE;
		}
		// Stop laser
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}

	@Override
	public void timerExpiredAction(final int locX, final int locY) {
		this.disruptionLeft--;
		if (this.disruptionLeft == 0) {
			SoundLoader.playSound(Sounds.DISRUPT_END);
			final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(new WoodenWall(), locX, locY, z, this.getLayer());
		} else {
			this.activateTimer(1);
		}
	}
}