/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.gameobject.Material;

public class MetallicRotaryMirror extends AbstractReactionWall {
    // Constructors
    public MetallicRotaryMirror() {
	this.setDirection(Direction.NORTH_EAST);
	this.setMaterial(Material.METALLIC);
    }

    @Override
    public final int getIdValue() {
	return 66;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	// Rotate mirror
	this.toggleDirection();
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().markAsDirty(locX + dirX, locY + dirY, locZ);
	return true;
    }
}
