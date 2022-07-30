/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class Ball extends AbstractMovableObject {
    // Constructors
    public Ball() {
	super(true);
	this.type.set(TypeConstants.TYPE_BALL);
	this.type.set(TypeConstants.TYPE_ICY);
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.SOUND_BALL_ROLL);
    }

    @Override
    public final int getStringBaseID() {
	return 2;
    }
}