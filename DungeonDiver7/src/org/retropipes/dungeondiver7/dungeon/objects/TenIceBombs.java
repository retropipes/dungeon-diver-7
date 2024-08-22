/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractInventoryModifier;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.PartyInventory;

public class TenIceBombs extends AbstractInventoryModifier {
    // Constructors
    public TenIceBombs() {
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getId() {
	return 55;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	PartyInventory.addTenIceBombs();
	GameLogic.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}
