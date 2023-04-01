/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class Wall extends AbstractWall {
    // Constructors
    public Wall() {
        this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
        this.setMaterial(Materials.METALLIC);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final int laserType, final int forceUnits) {
        switch (laserType) {
            case ShotTypes.DISRUPTOR:
                // Disrupt wall
                SoundLoader.playSound(Sounds.DISRUPTED);
                DungeonDiver7.getStuffBag().getGameLogic();
                GameLogic.morph(new DisruptedWall(), locX, locY, locZ, this.getLayer());
                return Direction.NONE;
            case ShotTypes.MISSILE:
                // Heat up wall
                SoundLoader.playSound(Sounds.MELT);
                DungeonDiver7.getStuffBag().getGameLogic();
                GameLogic.morph(new HotWall(), locX, locY, locZ, this.getLayer());
                return Direction.NONE;
            case ShotTypes.STUNNER: {
                // Freeze wall
                SoundLoader.playSound(Sounds.FROZEN);
                final var iw = new IcyWall();
                iw.setPreviousState(this);
                DungeonDiver7.getStuffBag().getGameLogic();
                GameLogic.morph(iw, locX, locY, locZ, this.getLayer());
                return Direction.NONE;
            }
            default:
                // Stop laser
                return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
        }
    }

    @Override
    public final int getBaseID() {
        return 45;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
        switch (materialID) {
            case Materials.ICE:
                final var iw = new IcyWall();
                iw.setPreviousState(this);
                return iw;
            case Materials.FIRE:
                return new HotWall();
            default:
                return this;
        }
    }
}