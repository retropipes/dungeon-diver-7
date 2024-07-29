/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractAttribute;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class Weakened extends AbstractAttribute {
    // Constructors
    public Weakened() {
    }

    @Override
    public final int getBaseID() {
	return 134;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Cracked(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(Sounds.EFFECT_CRACK);
	return Direction.NONE;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Cracked(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(Sounds.EFFECT_CRACK);
    }
}