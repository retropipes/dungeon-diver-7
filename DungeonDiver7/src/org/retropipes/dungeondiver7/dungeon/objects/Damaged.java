/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractAttribute;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class Damaged extends AbstractAttribute {
    // Constructors
    public Damaged() {
    }

    @Override
    public final int getIdValue() {
	return 133;
    }

    @Override
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	final var app = DungeonDiver7.getStuffBag();
	app.getGameLogic();
	GameLogic.morph(new Crumbling(), locX, locY, locZ, this.getLayer());
	SoundLoader.playSound(Sounds.EFFECT_CRACK);
    }
}