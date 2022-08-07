/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractAllButton;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;

public class WoodenAllButton extends AbstractAllButton {
    // Constructors
    public WoodenAllButton() {
	super(new WoodenAllButtonDoor(), false);
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
    }

    @Override
    public final int getStringBaseID() {
	return 116;
    }
}