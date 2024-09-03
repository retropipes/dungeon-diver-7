/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class MetallicBox extends AbstractMovableObject {
    // Constructors
    public MetallicBox() {
	super(true);
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var ib = new IcyBox();
	    ib.setPreviousStateObject(this);
	    yield ib;
	}
	case Material.FIRE -> new HotBox();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 73;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}