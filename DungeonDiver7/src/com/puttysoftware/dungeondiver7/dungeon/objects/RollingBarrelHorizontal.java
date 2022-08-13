/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class RollingBarrelHorizontal extends AbstractMovableObject {
    // Constructors
    public RollingBarrelHorizontal() {
	super(true);
	this.type.set(TypeConstants.TYPE_BARREL);
	this.type.set(TypeConstants.TYPE_ICY);
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.BALL_ROLL);
    }

    @Override
    public final int getBaseID() {
	return 140;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Direction dir = DirectionResolver.resolveRelativeDirection(dirX, dirY);
	if (dir == Direction.EAST || dir == Direction.WEST) {
	    // Roll
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	} else {
	    // Break up
	    final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	    // Boom!
	    SoundLoader.playSound(SoundConstants.BARREL);
	    // Destroy barrel
	    DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    // Check for tank in range of explosion
	    final boolean target = a.circularScanPlayer(locX, locY, locZ, 1);
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
	SoundLoader.playSound(SoundConstants.BARREL);
	// Destroy barrel
	DungeonDiver7.getApplication().getGameManager().morph(new Empty(), x, y, z, this.getLayer());
	// Check for tank in range of explosion
	final boolean target = a.circularScanPlayer(x, y, z, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getApplication().getGameManager().gameOver();
	}
    }
}