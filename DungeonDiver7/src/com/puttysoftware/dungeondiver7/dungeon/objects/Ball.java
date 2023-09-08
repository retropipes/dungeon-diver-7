/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class Ball extends AbstractMovableObject {
    // Constructors
    public Ball() {
	super(true);
	this.type.set(DungeonObjectTypes.TYPE_BALL);
	this.type.set(DungeonObjectTypes.TYPE_ICY);
    }

    @Override
    public final int getBaseID() {
	return 2;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.BALL_ROLL);
    }
}