/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class IcyBox extends AbstractMovableObject {
    // Constructors
    public IcyBox() {
	super(true);
	this.setMaterial(Material.ICE);
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case Material.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousStateObject();
	    }
	    return new Box();
	default:
	    return this;
	}
    }

    @Override
    public final int getIdValue() {
	return 21;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}