/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractAllButton;
import org.retropipes.dungeondiver7.utility.Materials;

public class IceAllButton extends AbstractAllButton {
    // Constructors
    public IceAllButton() {
	super(new IceAllButtonDoor(), false);
	this.setMaterial(Materials.ICE);
    }

    @Override
    public final int getIdValue() {
	return 80;
    }
}