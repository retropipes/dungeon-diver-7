/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;

public class IcyBox extends AbstractMovableObject {
    // Constructors
    public IcyBox() {
        super(true);
        this.type.set(DungeonObjectTypes.TYPE_BOX);
        this.type.set(DungeonObjectTypes.TYPE_ICY);
        this.setMaterial(Materials.ICE);
    }

    @Override
    public void playSoundHook() {
        SoundLoader.playSound(Sounds.PUSH_BOX);
    }

    @Override
    public final int getBaseID() {
        return 21;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
        switch (materialID) {
            case Materials.FIRE:
                if (this.hasPreviousState()) {
                    return this.getPreviousState();
                } else {
                    return new Box();
                }
            default:
                return this;
        }
    }
}