/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;

public class Barrel extends AbstractReactionWall {
    // Constructors
    public Barrel() {
	this.setMaterial(Material.WOODEN);
    }

    @Override
    public final int getIdValue() {
	return 3;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	// Boom!
	// Check for tank in range of explosion
	final var target = a.circularScanPlayer(locX + dirX, locY + dirY, locZ, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getStuffBag().getGameLogic().gameOver();
	    return true;
	}
	DungeonDiver7.getStuffBag().getGameLogic();
	// Destroy barrel
	GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	return true;
    }
}