/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTriggerButton;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;

public class IceTriggerButton extends AbstractTriggerButton {
    // Constructors
    public IceTriggerButton() {
	super(new IceTriggerButtonDoor(), false);
	this.setMaterial(MaterialConstants.MATERIAL_ICE);
    }

    @Override
    public final int getStringBaseID() {
	return 84;
    }
}