/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.Materials;

public class ThinIce extends AbstractGround {
    // Constructors
    public ThinIce() {
	super(false);
	this.setMaterial(Materials.ICE);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> {
	    final var i = new Ice();
	    i.setPreviousStateObject(this);
	    yield i;
	}
	case Materials.FIRE -> new Water();
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
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	DungeonDiver7.getStuffBag().getGameLogic().remoteDelayedDecayTo(new Water());
	return true;
    }
}