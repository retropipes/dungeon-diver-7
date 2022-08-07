/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;

public class Lava extends AbstractGround {
    // Constructors
    public Lava() {
	super();
	this.setMaterial(MaterialConstants.MATERIAL_FIRE);
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final Application app = DungeonDiver7.getApplication();
	if (pushed instanceof IcyBox) {
	    app.getGameManager().morph(new Ground(), x, y, z, this.getLayer());
	    SoundLoader.playSound(SoundConstants.SOUND_COOL_OFF);
	    return true;
	} else {
	    app.getGameManager().morph(new Empty(), x, y, z, pushed.getLayer());
	    SoundLoader.playSound(SoundConstants.SOUND_MELT);
	    return false;
	}
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    @Override
    public final int getStringBaseID() {
	return 62;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_ICE:
	    return new Ground();
	default:
	    return this;
	}
    }

    @Override
    public int getBlockHeight() {
	return -1;
    }
}
