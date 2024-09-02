/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTriggerButtonDoor;
import org.retropipes.dungeondiver7.gameobject.Material;

public class IceTriggerButtonDoor extends AbstractTriggerButtonDoor {
    // Constructors
    public IceTriggerButtonDoor() {
	this.setMaterial(Material.ICE);
    }

    @Override
    public final int getIdValue() {
	return 85;
    }
}