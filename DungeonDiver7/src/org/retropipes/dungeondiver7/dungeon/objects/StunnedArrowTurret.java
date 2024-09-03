/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public class StunnedArrowTurret extends AbstractMovableObject {
    private static final int STUNNED_START = 10;
    // Fields
    private int stunnedLeft;

    // Constructors
    public StunnedArrowTurret() {
	super(true);
	this.activateTimer(1);
	this.stunnedLeft = StunnedArrowTurret.STUNNED_START;
    }

    @Override
    public GameObject clone() {
	final var copy = (StunnedArrowTurret) super.clone();
	copy.stunnedLeft = this.stunnedLeft;
	return copy;
    }

    @Override
    public final int getIdValue() {
	return 34;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.stunnedLeft--;
	if (this.stunnedLeft == 1) {
	    this.activateTimer(1);
	} else if (this.stunnedLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    final var at = new ArrowTurret();
	    at.setSavedObject(this.getSavedObject());
	    at.setDirection(this.getDirection());
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(at, locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}
