/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractSpell;
import org.retropipes.dungeondiver7.game.GameLogic;

public class FreezeMagic extends AbstractSpell {
    // Constructors
    public FreezeMagic() {
    }

    @Override
    public final int getIdValue() {
	return 142;
    }

    // Scriptability
    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().fullScanFreezeGround();
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
    }
}