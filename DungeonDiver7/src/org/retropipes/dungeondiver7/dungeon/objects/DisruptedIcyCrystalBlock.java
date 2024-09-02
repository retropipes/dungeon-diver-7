/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionDisruptedObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.utility.RangeTypes;

public class DisruptedIcyCrystalBlock extends AbstractReactionDisruptedObject {
    private static final int DISRUPTION_START = 20;
    // Fields
    private int disruptionLeft;

    // Constructors
    public DisruptedIcyCrystalBlock() {
	this.disruptionLeft = DisruptedIcyCrystalBlock.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(Material.ICE);
    }

    @Override
    public final int getIdValue() {
	return 129;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (rangeType == RangeTypes.BOMB || RangeTypes.getMaterialForRangeType(rangeType) == Material.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy disrupted icy crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	}
	// Do nothing
	return true;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
	    final var icb = new IcyCrystalBlock();
	    if (this.hasPreviousState()) {
		icb.setPreviousStateObject(this.getPreviousStateObject());
	    }
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(icb, locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }
}