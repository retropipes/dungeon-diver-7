/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractAllButton;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class StoneAllButton extends AbstractAllButton {
    // Constructors
    public StoneAllButton() {
	super(new StoneAllButtonDoor(), false);
	this.setMaterial(Materials.STONE);
    }

    @Override
    public final int getBaseID() {
	return 104;
    }
}