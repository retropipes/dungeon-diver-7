/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTriggerButton;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class WoodenTriggerButton extends AbstractTriggerButton {
    // Constructors
    public WoodenTriggerButton() {
        super(new WoodenTriggerButtonDoor(), false);
        this.setMaterial(Materials.WOODEN);
    }

    @Override
    public final int getBaseID() {
        return 120;
    }
}