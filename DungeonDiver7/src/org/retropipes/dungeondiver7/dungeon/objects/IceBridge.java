/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class IceBridge extends AbstractGround {
    // Constructors
    public IceBridge() {
	super(false);
	this.setMaterial(Material.ICE);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	switch (materialID) {
	case Material.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousStateObject();
	    }
	    return new Bridge();
	default:
	    return this;
	}
    }

    @Override
    public final int getIdValue() {
	return 71;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(Sounds.PUSH);
    }

    @Override
    public boolean pushIntoAction(final GameObject pushed, final int x, final int y, final int z) {
	if (pushed instanceof HotBox) {
	    pushed.setSavedObject(new Bridge());
	}
	return true;
    }
}