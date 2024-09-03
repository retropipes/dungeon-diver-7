/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class ThinIce extends AbstractGround {
    // Constructors
    public ThinIce() {
	super(false);
	this.setMaterial(Material.ICE);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var i = new Ice();
	    i.setPreviousStateObject(this);
	    yield i;
	}
	case Material.FIRE -> new Water();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 43;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(Sounds.PUSH);
	DungeonDiver7.getStuffBag().getGameLogic().remoteDelayedDecayTo(new Water());
    }

    @Override
    public boolean pushIntoAction(final GameObject pushed, final int x, final int y, final int z) {
	DungeonDiver7.getStuffBag().getGameLogic().remoteDelayedDecayTo(new Water());
	return true;
    }
}