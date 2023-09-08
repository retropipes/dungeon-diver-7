/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class ThinIce extends AbstractGround {
    // Constructors
    public ThinIce() {
	super(false);
	this.setMaterial(Materials.ICE);
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> {
	    final var i = new Ice();
	    i.setPreviousState(this);
	    yield i;
	}
	case Materials.FIRE -> new Water();
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 43;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(Sounds.PUSH_MIRROR);
	DungeonDiver7.getStuffBag().getGameLogic().remoteDelayedDecayTo(new Water());
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	DungeonDiver7.getStuffBag().getGameLogic().remoteDelayedDecayTo(new Water());
	return true;
    }
}