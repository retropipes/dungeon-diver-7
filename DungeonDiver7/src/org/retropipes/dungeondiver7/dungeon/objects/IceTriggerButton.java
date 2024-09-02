/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTriggerButton;
import org.retropipes.dungeondiver7.gameobject.Material;

public class IceTriggerButton extends AbstractTriggerButton {
    // Constructors
    public IceTriggerButton() {
	super(new IceTriggerButtonDoor(), false);
	this.setMaterial(Material.ICE);
    }

    @Override
    public final int getIdValue() {
	return 84;
    }
}