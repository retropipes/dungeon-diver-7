/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;

public class Ice extends AbstractGround {
    public Ice() {
	super(false);
	this.setMaterial(Materials.ICE);
	this.type.set(DungeonObjectTypes.TYPE_ICY);
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case Materials.FIRE:
	    if (this.hasPreviousState()) {
		return this.getPreviousState();
	    }
	    return new Ground();
	default:
	    return this;
	}
    }

    @Override
    public final int getBaseID() {
	return 20;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(Sounds.PUSH);
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	if (pushed instanceof HotBox) {
	    final var g = new Ground();
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(g, x, y, z, g.getLayer());
	}
	return true;
    }
}
