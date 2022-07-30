/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractField;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;

public class FrostField extends AbstractField {
    // Constructors
    public FrostField() {
	super();
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	SoundLoader.playSound(SoundConstants.SOUND_FROZEN);
	DungeonDiver7.getApplication().getGameManager().updatePositionRelativeFrozen();
    }

    @Override
    public final int getStringBaseID() {
	return 14;
    }
}