/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class IceBridge extends AbstractGround {
	// Constructors
	public IceBridge() {
		super(false);
		this.setMaterial(Materials.ICE);
		this.type.set(DungeonObjectTypes.TYPE_ICY);
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		switch (materialID) {
		case Materials.FIRE:
			if (this.hasPreviousState()) {
				return this.getPreviousState();
			}
			return new Bridge();
		default:
			return this;
		}
	}

	@Override
	public final int getBaseID() {
		return 71;
	}

	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		SoundLoader.playSound(Sounds.PUSH_MIRROR);
	}

	@Override
	public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		if (pushed instanceof HotBox) {
			pushed.setSavedObject(new Bridge());
		}
		return true;
	}
}