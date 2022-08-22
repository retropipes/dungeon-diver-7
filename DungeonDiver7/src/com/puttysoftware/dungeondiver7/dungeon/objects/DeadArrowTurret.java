/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class DeadArrowTurret extends AbstractMovableObject {
    // Constructors
    public DeadArrowTurret() {
	super(false);
	this.setDirection(Directions.NORTH);
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	DungeonDiver7.getStuffBag().getGameLogic().haltMovingObjects();
	if (laserType != ShotTypes.MISSILE) {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	// Destroy
	SoundLoader.playSound(SoundConstants.BOOM);
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(this.getSavedObject(), locX, locY, locZ, this.getLayer());
	return Directions.NONE;
    }

    @Override
    public void playSoundHook() {
	// Do nothing
    }

    @Override
    public final int getBaseID() {
	return 11;
    }
}
