/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
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
	final StuffBag app = DungeonDiver7.getStuffBag();
	if (pushed instanceof IcyBox) {
	    app.getGameLogic();
	    GameLogic.morph(new Ground(), x, y, z, this.getLayer());
	    SoundLoader.playSound(SoundConstants.COOL_OFF);
	    return true;
	} else {
	    app.getGameLogic();
	    GameLogic.morph(new Empty(), x, y, z, pushed.getLayer());
	    SoundLoader.playSound(SoundConstants.MELT);
	    return false;
	}
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    @Override
    public final int getBaseID() {
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
