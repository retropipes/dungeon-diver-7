/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class RollingBarrelVertical extends AbstractMovableObject {
    // Constructors
    public RollingBarrelVertical() {
	super(true);
	this.type.set(TypeConstants.TYPE_BARREL);
	this.type.set(TypeConstants.TYPE_ICY);
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.SOUND_BALL_ROLL);
    }

    @Override
    public final int getStringBaseID() {
	return 141;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Direction dir = DirectionResolver.resolveRelativeDirection(dirX, dirY);
	if (dir == Direction.NORTH || dir == Direction.SOUTH) {
	    // Roll
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	} else {
	    // Break up
	    final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	    // Boom!
	    SoundLoader.playSound(SoundConstants.SOUND_BARREL);
	    // Destroy barrel
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    // Check for tank in range of explosion
	    final boolean target = a.circularScanTank(locX, locY, locZ, 1);
	    if (target) {
		// Kill tank
		DungeonDiver7.getApplication().getGameManager().gameOver();
	    }
	    if (laserType == ArrowTypeConstants.LASER_TYPE_POWER) {
		// Laser keeps going
		return DirectionResolver.resolveRelativeDirection(dirX, dirY);
	    } else {
		// Laser stops
		return Direction.NONE;
	    }
	}
    }

    @Override
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Break up
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	// Boom!
	SoundLoader.playSound(SoundConstants.SOUND_BARREL);
	// Destroy barrel
	DungeonDiver7.getApplication().getGameManager().morph(new Empty(), x, y, z, this.getLayer());
	// Check for tank in range of explosion
	final boolean target = a.circularScanTank(x, y, z, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getApplication().getGameManager().gameOver();
	}
    }
}