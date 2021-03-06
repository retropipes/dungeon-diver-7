/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class DeeperWater extends AbstractGround {
    // Constructors
    public DeeperWater() {
	super();
	this.setFrameNumber(1);
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final Application app = DungeonDiver7.getApplication();
	if (pushed.isOfType(TypeConstants.TYPE_BOX)) {
	    // Get rid of pushed object
	    app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
	    if (pushed.getMaterial() == MaterialConstants.MATERIAL_WOODEN) {
		app.getGameManager().morph(new Bridge(), x, y, z, this.getLayer());
	    } else {
		app.getGameManager().morph(new DeepWater(), x, y, z, this.getLayer());
	    }
	} else {
	    app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
	}
	SoundLoader.playSound(SoundConstants.SOUND_SINK);
	return false;
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    @Override
    public final int getStringBaseID() {
	return 68;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_ICE:
	    final Ice i = new Ice();
	    i.setPreviousState(this);
	    return i;
	case MaterialConstants.MATERIAL_FIRE:
	    return new DeepWater();
	default:
	    return this;
	}
    }

    @Override
    public int getBlockHeight() {
	return -3;
    }
}
