/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractAllButton;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class UniversalAllButton extends AbstractAllButton {
	// Constructors
	public UniversalAllButton() {
		super(new UniversalAllButtonDoor(), true);
		this.setMaterial(Materials.DEFAULT);
	}

	@Override
	public final int getBaseID() {
		return 110;
	}
}