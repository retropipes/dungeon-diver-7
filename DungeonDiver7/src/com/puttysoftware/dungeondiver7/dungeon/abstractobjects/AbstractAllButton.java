/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public abstract class AbstractAllButton extends AbstractButton {
	// Constructors
	protected AbstractAllButton(final AbstractAllButtonDoor abd, final boolean isUniversal) {
		super(abd, isUniversal);
		this.type.set(TypeConstants.TYPE_ALL_BUTTON);
	}

	@Override
	public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
			SoundLoader.playSound(SoundConstants.SOUND_BUTTON);
			if (!this.isTriggered()) {
				// Check to open door at location
				this.setTriggered(true);
				DungeonDiver7.getApplication().getDungeonManager().getDungeon().fullScanAllButtonOpen(z, this);
			}
		}
		return true;
	}

	@Override
	public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
		if (this.isUniversal() || pushed.getMaterial() == this.getMaterial()) {
			if (this.isTriggered()) {
				// Check to close door at location
				this.setTriggered(false);
				DungeonDiver7.getApplication().getDungeonManager().getDungeon().fullScanAllButtonClose(z, this);
			}
		}
	}
}
