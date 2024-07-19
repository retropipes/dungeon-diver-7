/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTriggerButtonDoor;
import org.retropipes.dungeondiver7.utility.Materials;

public class PlasticTriggerButtonDoor extends AbstractTriggerButtonDoor {
	// Constructors
	public PlasticTriggerButtonDoor() {
		this.setMaterial(Materials.PLASTIC);
	}

	@Override
	public final int getBaseID() {
		return 103;
	}
}