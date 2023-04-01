/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class WoodenBox extends AbstractMovableObject {
    // Constructors
    public WoodenBox() {
        super(true);
        this.type.set(DungeonObjectTypes.TYPE_BOX);
        this.setMaterial(Materials.WOODEN);
    }

    @Override
    public void playSoundHook() {
        SoundLoader.playSound(Sounds.PUSH_BOX);
    }

    @Override
    public final int getBaseID() {
        return 70;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
        switch (materialID) {
            case Materials.ICE:
                final var ib = new IcyBox();
                ib.setPreviousState(this);
                return ib;
            case Materials.FIRE:
                return new Ground();
            default:
                return this;
        }
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final int laserType, final int forceUnits) {
        final var app = DungeonDiver7.getStuffBag();
        if (forceUnits < this.getMinimumReactionForce()) {
            // Not enough force
            return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
        }
        final var mof = app.getDungeonManager().getDungeon().getCell(locX + dirX, locY + dirY, locZ, this.getLayer());
        final var mor = app.getDungeonManager().getDungeon().getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
        if (laserType == ShotTypes.MISSILE) {
            // Destroy wooden box
            SoundLoader.playSound(Sounds.BARREL);
            app.getGameLogic();
            GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
        } else {
            if (laserType == ShotTypes.BLUE && mor != null
                    && (mor.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mor.isSolid())) {
                app.getGameLogic().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
            } else if (mof != null && (mof.isOfType(DungeonObjectTypes.TYPE_CHARACTER) || !mof.isSolid())) {
                app.getGameLogic().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
            } else {
                // Object doesn't react to this type of laser
                return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
            }
            this.playSoundHook();
        }
        return Direction.NONE;
    }
}