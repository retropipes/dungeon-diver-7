/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class StunnedArrowTurret extends AbstractMovableObject {
    // Fields
    private int stunnedLeft;
    private static final int STUNNED_START = 10;

    // Constructors
    public StunnedArrowTurret() {
        super(true);
        this.activateTimer(1);
        this.stunnedLeft = StunnedArrowTurret.STUNNED_START;
        this.type.set(DungeonObjectTypes.TYPE_ANTI);
    }

    @Override
    public AbstractDungeonObject clone() {
        final var copy = (StunnedArrowTurret) super.clone();
        copy.stunnedLeft = this.stunnedLeft;
        return copy;
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
        this.stunnedLeft--;
        if (this.stunnedLeft == 1) {
            SoundLoader.playSound(Sounds.STUN_OFF);
            this.activateTimer(1);
        } else if (this.stunnedLeft == 0) {
            final var z = DungeonDiver7.getStuffBag().getGameLogic().getPlayerManager().getPlayerLocationZ();
            final var at = new ArrowTurret();
            at.setSavedObject(this.getSavedObject());
            at.setDirection(this.getDirection());
            DungeonDiver7.getStuffBag().getGameLogic();
            GameLogic.morph(at, locX, locY, z, this.getLayer());
        } else {
            SoundLoader.playSound(Sounds.STUNNED);
            this.activateTimer(1);
        }
    }

    @Override
    public void playSoundHook() {
        SoundLoader.playSound(Sounds.PUSH_ANTI_TANK);
    }

    @Override
    public final int getBaseID() {
        return 34;
    }
}
