/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractSpell;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.loader.Sounds;

public class FreezeMagic extends AbstractSpell {
	// Constructors
	public FreezeMagic() {
	}

	@Override
	public final int getBaseID() {
		return 142;
	}

	// Scriptability
	@Override
	public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
		SoundLoader.playSound(Sounds.FREEZE_MAGIC);
		DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().fullScanFreezeGround();
		DungeonDiver7.getStuffBag().getGameLogic();
		GameLogic.morph(new Empty(), dirX, dirY, dirZ, this.getLayer());
	}
}