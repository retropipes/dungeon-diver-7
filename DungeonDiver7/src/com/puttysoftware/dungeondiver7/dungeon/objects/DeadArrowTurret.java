/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;

public class DeadArrowTurret extends AbstractMovableObject {
    // Constructors
    public DeadArrowTurret() {
	super(false);
	this.setDirection(Direction.NORTH);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	DungeonDiver7.getApplication().getGameManager().haltMovingObjects();
	if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
	    // Destroy
	    SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	    DungeonDiver7.getApplication().getGameManager().morph(this.getSavedObject(), locX, locY, locZ,
		    this.getLayer());
	} else {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	// Do nothing
    }

    @Override
    public final int getStringBaseID() {
	return 11;
    }
}
