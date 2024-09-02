/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.game.GameLogic;

public class RollingBarrelHorizontal extends AbstractMovableObject {
    // Constructors
    public RollingBarrelHorizontal() {
	super(true);
    }

    @Override
    public final int getIdValue() {
	return 140;
    }

    @Override
    public void playSoundHook() {
    }

    @Override
    public void pushCollideAction(final DungeonObject pushed, final int x, final int y, final int z) {
	// Break up
	final var a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	// Boom!
	DungeonDiver7.getStuffBag().getGameLogic();
	// Destroy barrel
	GameLogic.morph(new Empty(), x, y, z, this.getLayer());
	// Check for tank in range of explosion
	final var target = a.circularScanPlayer(x, y, z, 1);
	if (target) {
	    // Kill tank
	    DungeonDiver7.getStuffBag().getGameLogic().gameOver();
	}
    }
}