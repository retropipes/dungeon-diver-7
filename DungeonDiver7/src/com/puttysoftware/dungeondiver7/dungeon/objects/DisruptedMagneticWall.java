/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDisruptedObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class DisruptedMagneticWall extends AbstractDisruptedObject {
	// Fields
	private int disruptionLeft;
	private static final int DISRUPTION_START = 20;

	// Constructors
	public DisruptedMagneticWall() {
		this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
		this.disruptionLeft = DisruptedMagneticWall.DISRUPTION_START;
		this.activateTimer(1);
		this.setMaterial(Materials.MAGNETIC);
	}

	@Override
	public void timerExpiredAction(final int locX, final int locY) {
		this.disruptionLeft--;
		if (this.disruptionLeft == 0) {
			SoundLoader.playSound(Sounds.DISRUPT_END);
			final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(new MagneticWall(), locX, locY, z, this.getLayer());
		} else {
			this.activateTimer(1);
		}
	}

	@Override
	public final int getBaseID() {
		return 50;
	}
}