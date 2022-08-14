/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.BagOStuff;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractTriggerButton extends AbstractButton {
    // Constructors
    protected AbstractTriggerButton(final AbstractTriggerButtonDoor tbd, final boolean isUniversal) {
	super(tbd, isUniversal);
	this.type.set(TypeConstants.TYPE_TRIGGER_BUTTON);
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final BagOStuff app = DungeonDiver7.getApplication();
	if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
	    SoundLoader.playSound(SoundConstants.BUTTON);
	    if (this.isTriggered()) {
		// Close door at location
		app.getGameManager().morph(this.getButtonDoor(), this.getDoorX(), this.getDoorY(), z, this.getLayer());
		SoundLoader.playSound(SoundConstants.DOOR_CLOSES);
		this.setTriggered(false);
	    } else {
		// Open door at location
		app.getGameManager().morph(new Empty(), this.getDoorX(), this.getDoorY(), z, this.getLayer());
		SoundLoader.playSound(SoundConstants.DOOR_OPENS);
		this.setTriggered(true);
	    }
	}
	return true;
    }

    @Override
    public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }
}
