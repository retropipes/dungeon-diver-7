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

public class Barrel extends AbstractReactionWall {
    // Constructors
    public Barrel() {
	super();
	this.type.set(TypeConstants.TYPE_BARREL);
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
	    final int dirY, final int laserType, final int forceUnits) {
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

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	// Boom!
	SoundLoader.playSound(SoundConstants.SOUND_BARREL);
	// Check for tank in range of explosion
	final boolean target = a.circularScanTank(locX + dirX, locY + dirY, locZ, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getApplication().getGameManager().gameOver();
	    return true;
	}
	// Destroy barrel
	DungeonDiver7.getApplication().getGameManager().morph(new Empty(), locX + dirX, locY + dirY, locZ,
		this.getLayer());
	return true;
    }

    @Override
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// React to balls hitting barrels
	if (pushed.isOfType(TypeConstants.TYPE_BALL)) {
	    this.laserEnteredAction(x, y, z, 0, 0, ArrowTypeConstants.LASER_TYPE_GREEN, 1);
	}
    }

    @Override
    public final int getStringBaseID() {
	return 3;
    }
}