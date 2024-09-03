/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractGround;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class DeeperWater extends AbstractGround {
    // Constructors
    public DeeperWater() {
	this.setFrameNumber(1);
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var i = new Ice();
	    i.setPreviousStateObject(this);
	    yield i;
	}
	case Material.FIRE -> new DeepWater();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 68;
    }

    @Override
    public int getHeight() {
	return -3;
    }

    @Override
    public boolean killsOnMove() {
	return true;
    }

    // Scriptability
    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	if (pushed.canMove()) {
	    app.getGameLogic();
	    // Get rid of pushed object
	    GameLogic.morph(new Empty(), x, y, z, pushed.getLayer());
	    if (pushed.getMaterial() == Material.WOODEN) {
		app.getGameLogic();
		GameLogic.morph(new Bridge(), x, y, z, this.getLayer());
	    } else {
		app.getGameLogic();
		GameLogic.morph(new DeepWater(), x, y, z, this.getLayer());
	    }
	} else {
	    app.getGameLogic();
	    GameLogic.morph(new Empty(), x, y, z, pushed.getLayer());
	}
	SoundLoader.playSound(Sounds.SINK);
	return false;
    }
}
