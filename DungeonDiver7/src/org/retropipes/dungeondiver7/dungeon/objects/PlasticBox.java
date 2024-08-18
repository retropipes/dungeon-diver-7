/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;

public class PlasticBox extends AbstractMovableObject {
    // Constructors
    public PlasticBox() {
	super(true);
	this.type.set(DungeonObjectTypes.TYPE_BOX);
	this.setMaterial(Materials.PLASTIC);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> {
	    final var ib = new IcyBox();
	    ib.setPreviousState(this);
	    yield ib;
	}
	case Materials.FIRE -> new HotBox();
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 72;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}