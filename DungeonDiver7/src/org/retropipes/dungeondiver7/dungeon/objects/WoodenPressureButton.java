/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractPressureButton;
import org.retropipes.dungeondiver7.utility.Materials;

public class WoodenPressureButton extends AbstractPressureButton {
    // Constructors
    public WoodenPressureButton() {
	super(new WoodenPressureButtonDoor(), false);
	this.setMaterial(Materials.WOODEN);
    }

    @Override
    public final int getBaseID() {
	return 118;
    }
}