/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;

public class ThinIce extends AbstractGround {
    // Constructors
    public ThinIce() {
	super(false);
	this.setMaterial(MaterialConstants.MATERIAL_ICE);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(SoundConstants.SOUND_PUSH_MIRROR);
	DungeonDiver7.getApplication().getGameManager().remoteDelayedDecayTo(new Water());
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	DungeonDiver7.getApplication().getGameManager().remoteDelayedDecayTo(new Water());
	return true;
    }

    @Override
    public final int getStringBaseID() {
	return 43;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_ICE:
	    final Ice i = new Ice();
	    i.setPreviousState(this);
	    return i;
	case MaterialConstants.MATERIAL_FIRE:
	    return new Water();
	default:
	    return this;
	}
    }
}