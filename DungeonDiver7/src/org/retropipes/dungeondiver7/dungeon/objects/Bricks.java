/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;

public class Bricks extends AbstractReactionWall {
    // Constructors
    public Bricks() {
	this.setMaterial(Material.PLASTIC);
    }

    @Override
    public final int getIdValue() {
	return 8;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	return true;
    }
}