/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class ExplodingBarrel extends AbstractReactionWall {
    // Fields
    private boolean destroyed;

    // Constructors
    public ExplodingBarrel() {
	super();
	this.type.set(TypeConstants.TYPE_BARREL);
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
	this.destroyed = false;
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	// Boom!
	SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	// Did tank die?
	final boolean dead = this.laserEnteredActionInnerP1(locX, locY, locZ, false);
	if (dead) {
	    // Kill tank
	    DungeonDiver7.getApplication().getGameManager().gameOver();
	    return Direction.NONE;
	}
	ExplodingBarrel.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	if (laserType == ArrowTypeConstants.LASER_TYPE_POWER) {
	    // Laser keeps going
	    return DirectionResolver.resolveRelativeDirection(dirX, dirY);
	} else {
	    // Laser stops
	    return Direction.NONE;
	}
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	// Boom!
	SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	// Did tank die?
	final boolean dead = this.laserEnteredActionInnerP1(locX + dirX, locY + dirY, locZ, false);
	if (dead) {
	    // Kill tank
	    DungeonDiver7.getApplication().getGameManager().gameOver();
	    return true;
	}
	// Destroy barrel
	ExplodingBarrel.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	return true;
    }

    private boolean laserEnteredActionInnerP1(final int locX, final int locY, final int locZ, final boolean oldDead) {
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	boolean dead = oldDead;
	// Check if this barrel's been destroyed already
	if (this.destroyed) {
	    return dead;
	}
	// Check for tank in range of explosion
	if (!dead) {
	    dead = a.circularScanTank(locX, locY, locZ, 1);
	}
	// Set destroyed status
	this.destroyed = true;
	// Check for nearby exploding barrels and blow them up too
	final boolean boom2 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		.getCell(locX, locY - 1, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom2) {
	    return this.laserEnteredActionInnerP1(locX, locY - 1, locZ, dead);
	}
	final boolean boom4 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		.getCell(locX - 1, locY, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom4) {
	    return this.laserEnteredActionInnerP1(locX - 1, locY, locZ, dead);
	}
	final boolean boom6 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		.getCell(locX + 1, locY, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom6) {
	    return this.laserEnteredActionInnerP1(locX + 1, locY, locZ, dead);
	}
	final boolean boom8 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		.getCell(locX, locY + 1, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom8) {
	    return this.laserEnteredActionInnerP1(locX, locY + 1, locZ, dead);
	}
	// Communicate tank dead status back to caller
	return dead;
    }

    private static void laserEnteredActionInnerP2(final int locX, final int locY, final int locZ, final int locW) {
	// Destroy barrel
	DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX, locY, locZ, locW);
	// Check for nearby exploding barrels and blow them up too
	try {
	    final boolean boom2 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		    .getCell(locX, locY - 1, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom2) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX, locY - 1, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final boolean boom4 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		    .getCell(locX - 1, locY, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom4) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX - 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final boolean boom6 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		    .getCell(locX + 1, locY, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom6) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX + 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final boolean boom8 = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
		    .getCell(locX, locY + 1, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom8) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX, locY + 1, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
    }

    @Override
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// React to balls hitting exploding barrels
	if (pushed.isOfType(TypeConstants.TYPE_BALL)) {
	    this.laserEnteredAction(x, y, z, 0, 0, ArrowTypeConstants.LASER_TYPE_GREEN, 1);
	}
    }

    @Override
    public final int getStringBaseID() {
	return 12;
    }
}