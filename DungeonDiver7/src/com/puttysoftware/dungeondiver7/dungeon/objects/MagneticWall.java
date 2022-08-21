/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class MagneticWall extends AbstractWall {
    // Constructors
    public MagneticWall() {
	super();
	this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	this.setMaterial(Materials.MAGNETIC);
    }

    @Override
    public Directions laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (laserType == ShotTypes.DISRUPTOR) {
	    // Disrupt magnetic wall
	    SoundLoader.playSound(SoundConstants.DISRUPTED);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(new DisruptedMagneticWall(), locX, locY, locZ, this.getLayer());
	    return Directions.NONE;
	} else {
	    // Stop laser
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
    }

    @Override
    public final int getBaseID() {
	return 24;
    }
}