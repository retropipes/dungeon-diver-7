/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class Box extends AbstractMovableObject {
	// Constructors
	public Box() {
		super(true);
		this.type.set(DungeonObjectTypes.TYPE_BOX);
		this.setMaterial(Materials.STONE);
	}

	@Override
	public void playSoundHook() {
		SoundLoader.playSound(Sounds.PUSH_BOX);
	}

	@Override
	public final int getBaseID() {
		return 7;
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		switch (materialID) {
			case Materials.ICE:
				final var ib = new IcyBox();
				ib.setPreviousState(this);
				return ib;
			case Materials.FIRE:
				return new HotBox();
			default:
				return this;
		}
	}
}