/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractPressureButton;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class IcePressureButton extends AbstractPressureButton {
    // Constructors
    public IcePressureButton() {
        super(new IcePressureButtonDoor(), false);
        this.setMaterial(Materials.ICE);
    }

    @Override
    public final int getBaseID() {
        return 82;
    }
}