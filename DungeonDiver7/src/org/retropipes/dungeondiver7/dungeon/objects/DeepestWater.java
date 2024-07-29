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

public class DeepestWater extends AbstractGround {
    // Constructors
    public DeepestWater() {
	this.setFrameNumber(1);
	this.setMaterial(Materials.WOODEN);
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	return switch (materialID) {
	case Materials.ICE -> {
	    final var i = new Ice();
	    i.setPreviousState(this);
	    yield i;
	}
	case Materials.FIRE -> new DeeperWater();
	default -> this;
	};
    }

    @Override
    public final int getBaseID() {
	return 69;
    }

    @Override
    public int getBlockHeight() {
	return -4;
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	// Get rid of pushed object
	GameLogic.morph(new Empty(), x, y, z, pushed.getLayer());
	if (pushed.isOfType(DungeonObjectTypes.TYPE_BOX)) {
	    if (pushed.getMaterial() == Materials.WOODEN) {
		app.getGameLogic();
		GameLogic.morph(new Bridge(), x, y, z, this.getLayer());
	    } else {
		app.getGameLogic();
		GameLogic.morph(new DeeperWater(), x, y, z, this.getLayer());
	    }
	}
	SoundLoader.playSound(Sounds.SINK);
	return false;
    }
}
