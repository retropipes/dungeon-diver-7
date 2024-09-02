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

public class WoodenBox extends AbstractMovableObject {
    // Constructors
    public WoodenBox() {
	super(true);
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public DungeonObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var ib = new IcyBox();
	    ib.setPreviousStateObject(this);
	    yield ib;
	}
	case Material.FIRE -> new Ground();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 70;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}