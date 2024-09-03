/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

public abstract class AbstractTriggerButton extends AbstractButton {
    // Constructors
    protected AbstractTriggerButton(final AbstractTriggerButtonDoor tbd, final boolean isUniversal) {
	super(tbd, isUniversal);
    }

    @Override
    public boolean pushIntoAction(final GameObject pushed, final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	if (this.isBoundUniversally() || pushed.getMaterial() == this.getMaterial()) {
	    SoundLoader.playSound(Sounds.BUTTON);
	    if (this.isTriggered()) {
		app.getGameLogic();
		// Close door at location
		GameLogic.morph(this.getBoundObject(), this.getBoundObjectX(), this.getBoundObjectY(), z, this.getLayer());
		SoundLoader.playSound(Sounds.DOOR_CLOSES);
		this.setTriggered(false);
	    } else {
		app.getGameLogic();
		// Open door at location
		GameLogic.morph(new Empty(), this.getBoundObjectX(), this.getBoundObjectY(), z, this.getLayer());
		SoundLoader.playSound(Sounds.DOOR_OPENS);
		this.setTriggered(true);
	    }
	}
	return true;
    }

    @Override
    public void pushOutAction(final GameObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }
}
