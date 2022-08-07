/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractPressureButton extends AbstractButton {
    // Constructors
    protected AbstractPressureButton(final AbstractPressureButtonDoor pbd, final boolean isUniversal) {
	super(pbd, isUniversal);
	this.type.set(TypeConstants.TYPE_PRESSURE_BUTTON);
    }

    @Override
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final Application app = DungeonDiver7.getApplication();
	if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
	    SoundLoader.playSound(SoundConstants.BUTTON);
	    if (!this.isTriggered()) {
		// Open door at location
		app.getGameManager().morph(new Empty(), this.getDoorX(), this.getDoorY(), z, this.getLayer());
		SoundLoader.playSound(SoundConstants.DOOR_OPENS);
		this.setTriggered(true);
	    } else {
		// Close door at location
		app.getGameManager().morph(this.getButtonDoor(), this.getDoorX(), this.getDoorY(), z, this.getLayer());
		SoundLoader.playSound(SoundConstants.DOOR_CLOSES);
		this.setTriggered(false);
	    }
	}
	return true;
    }

    @Override
    public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	final Application app = DungeonDiver7.getApplication();
	if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
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
    }
}
