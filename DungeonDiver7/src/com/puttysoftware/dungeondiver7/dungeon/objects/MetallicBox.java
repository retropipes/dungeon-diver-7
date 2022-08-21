/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class MetallicBox extends AbstractMovableObject {
    // Constructors
    public MetallicBox() {
	super(true);
	this.type.set(DungeonObjectTypes.TYPE_BOX);
	this.setMaterial(Materials.METALLIC);
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.PUSH_BOX);
    }

    @Override
    public final int getBaseID() {
	return 73;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.ICE:
	    final IcyBox ib = new IcyBox();
	    ib.setPreviousState(this);
	    return ib;
	case Materials.FIRE:
	    return new HotBox();
	default:
	    return this;
	}
    }
}