/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractGround;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class Ice extends AbstractGround {
    public Ice() {
	super(false);
	this.setMaterial(MaterialConstants.MATERIAL_ICE);
	this.type.set(TypeConstants.TYPE_ICY);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(SoundConstants.SOUND_PUSH_MIRROR);
    }

    @Override
    public final int getStringBaseID() {
	return 20;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    } else {
		return new Ground();
	    }
	default:
	    return this;
	}
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	if (pushed instanceof HotBox) {
	    final Ground g = new Ground();
	    DungeonDiver7.getApplication().getGameManager().morph(g, x, y, z, g.getLayer());
	    SoundLoader.playSound(SoundConstants.SOUND_DEFROST);
	}
	return true;
    }
}
