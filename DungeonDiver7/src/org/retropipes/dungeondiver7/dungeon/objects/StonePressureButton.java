/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractPressureButton;
import org.retropipes.dungeondiver7.utility.Materials;

public class StonePressureButton extends AbstractPressureButton {
    // Constructors
    public StonePressureButton() {
	super(new StonePressureButtonDoor(), false);
	this.setMaterial(Materials.STONE);
    }

    @Override
    public final int getBaseID() {
	return 106;
    }
}