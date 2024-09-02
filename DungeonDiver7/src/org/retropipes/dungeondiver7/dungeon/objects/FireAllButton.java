/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractAllButton;
import org.retropipes.dungeondiver7.gameobject.Material;

public class FireAllButton extends AbstractAllButton {
    // Constructors
    public FireAllButton() {
	super(new FireAllButtonDoor(), false);
	this.setMaterial(Material.FIRE);
    }

    @Override
    public final int getIdValue() {
	return 74;
    }
}