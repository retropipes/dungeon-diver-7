/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractInventoryModifier;
import com.puttysoftware.dungeondiver7.game.GameManager;
import com.puttysoftware.dungeondiver7.utilities.PartyInventory;

public class TenBombs extends AbstractInventoryModifier {
    // Constructors
    public TenBombs() {
	super();
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	final GameManager gm = DungeonDiver7.getApplication().getGameManager();
	PartyInventory.addTenBombs();
	gm.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }

    @Override
    public boolean doLasersPassThrough() {
	return true;
    }

    @Override
    public final int getStringBaseID() {
	return 53;
    }
}
