/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class IcyBox extends AbstractMovableObject {
	// Constructors
	public IcyBox() {
		super(true);
		this.type.set(TypeConstants.TYPE_BOX);
		this.type.set(TypeConstants.TYPE_ICY);
		this.setMaterial(MaterialConstants.MATERIAL_ICE);
	}

	@Override
	public void playSoundHook() {
		SoundLoader.playSound(SoundConstants.SOUND_PUSH_BOX);
	}

	@Override
	public final int getStringBaseID() {
		return 21;
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		switch (materialID) {
		case MaterialConstants.MATERIAL_FIRE:
			if (this.hasPreviousState()) {
				return this.getPreviousState();
			} else {
				return new Box();
			}
		default:
			return this;
		}
	}
}