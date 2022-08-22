/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class ExplodingBarrel extends AbstractReactionWall {
    // Fields
    private boolean destroyed;

    // Constructors
    public ExplodingBarrel() {
	this.type.set(DungeonObjectTypes.TYPE_BARREL);
	this.setMaterial(Materials.WOODEN);
	this.destroyed = false;
    }

    @Override
    public Directions laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
	// Boom!
	SoundLoader.playSound(SoundConstants.BOOM);
	// Did tank die?
	final var dead = this.laserEnteredActionInnerP1(locX, locY, locZ, false);
	if (dead) {
	    // Kill tank
	    DungeonDiver7.getStuffBag().getGameLogic().gameOver();
	    return Directions.NONE;
	}
	ExplodingBarrel.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	if (laserType == ShotTypes.POWER) {
	    // Laser keeps going
	    return DirectionResolver.resolve(dirX, dirY);
	}
	// Laser stops
	return Directions.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	// Boom!
	SoundLoader.playSound(SoundConstants.BOOM);
	// Did tank die?
	final var dead = this.laserEnteredActionInnerP1(locX + dirX, locY + dirY, locZ, false);
	if (dead) {
	    // Kill tank
	    DungeonDiver7.getStuffBag().getGameLogic().gameOver();
	    return true;
	}
	// Destroy barrel
	ExplodingBarrel.laserEnteredActionInnerP2(locX, locY, locZ, this.getLayer());
	return true;
    }

    private boolean laserEnteredActionInnerP1(final int locX, final int locY, final int locZ, final boolean oldDead) {
	final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	var dead = oldDead;
	// Check if this barrel's been destroyed already
	if (this.destroyed) {
	    return dead;
	}
	// Check for tank in range of explosion
	if (!dead) {
	    dead = a.circularScanPlayer(locX, locY, locZ, 1);
	}
	// Set destroyed status
	this.destroyed = true;
	// Check for nearby exploding barrels and blow them up too
	final var boom2 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		.getCell(locX, locY - 1, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom2) {
	    return this.laserEnteredActionInnerP1(locX, locY - 1, locZ, dead);
	}
	final var boom4 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		.getCell(locX - 1, locY, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom4) {
	    return this.laserEnteredActionInnerP1(locX - 1, locY, locZ, dead);
	}
	final var boom6 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		.getCell(locX + 1, locY, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom6) {
	    return this.laserEnteredActionInnerP1(locX + 1, locY, locZ, dead);
	}
	final var boom8 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		.getCell(locX, locY + 1, locZ, this.getLayer()).getClass().equals(ExplodingBarrel.class);
	if (boom8) {
	    return this.laserEnteredActionInnerP1(locX, locY + 1, locZ, dead);
	}
	// Communicate tank dead status back to caller
	return dead;
    }

    private static void laserEnteredActionInnerP2(final int locX, final int locY, final int locZ, final int locW) {
	DungeonDiver7.getStuffBag().getGameLogic();
	// Destroy barrel
	GameLogic.morph(new Empty(), locX, locY, locZ, locW);
	// Check for nearby exploding barrels and blow them up too
	try {
	    final var boom2 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		    .getCell(locX, locY - 1, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom2) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX, locY - 1, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final var boom4 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		    .getCell(locX - 1, locY, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom4) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX - 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final var boom6 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
		    .getCell(locX + 1, locY, locZ, locW).getClass().equals(ExplodingBarrel.class);
	    if (boom6) {
		ExplodingBarrel.laserEnteredActionInnerP2(locX + 1, locY, locZ, locW);
	    }
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    // Ignore
	}
	try {
	    final var boom8 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
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
	if (pushed.isOfType(DungeonObjectTypes.TYPE_BALL)) {
	    this.laserEnteredAction(x, y, z, 0, 0, ShotTypes.GREEN, 1);
	}
    }

    @Override
    public final int getBaseID() {
	return 12;
    }
}