/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.Materials;

public class Lava extends AbstractGround {
    // Constructors
    public Lava() {
	this.setMaterial(Materials.FIRE);
    }

    @Override
    public DungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> new Ground();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 62;
    }

    @Override
    public int getHeight() {
	return -1;
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	if (pushed instanceof IcyBox) {
	    app.getGameLogic();
	    GameLogic.morph(new Ground(), x, y, z, this.getLayer());
	    return true;
	}
	app.getGameLogic();
	GameLogic.morph(new Empty(), x, y, z, pushed.getLayer());
	return false;
    }
}
