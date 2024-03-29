/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractReactionWall;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.RangeTypes;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;

public class IcyCrystalBlock extends AbstractReactionWall {
    // Constructors
    public IcyCrystalBlock() {
        this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
        this.setMaterial(Materials.ICE);
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
            final int dirY, final int laserType, final int forceUnits) {
        if (laserType == ShotTypes.MISSILE) {
            // Destroy icy crystal block
            SoundLoader.playSound(Sounds.BOOM);
            DungeonDiver7.getStuffBag().getGameLogic();
            GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
            return Direction.NONE;
        }
        if (laserType == ShotTypes.DISRUPTOR) {
            // Disrupt icy crystal block
            SoundLoader.playSound(Sounds.DISRUPTED);
            final var dicb = new DisruptedIcyCrystalBlock();
            if (this.hasPreviousState()) {
                dicb.setPreviousState(this.getPreviousState());
            }
            DungeonDiver7.getStuffBag().getGameLogic();
            GameLogic.morph(dicb, locX, locY, locZ, this.getLayer());
        } else {
            // Stop laser
            SoundLoader.playSound(Sounds.LASER_DIE);
        }
        return Direction.NONE;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final int rangeType, final int forceUnits) {
        if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
            DungeonDiver7.getStuffBag().getGameLogic();
            // Destroy icy crystal block
            GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
            return true;
        }
        if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE) {
            // Heat up crystal block
            SoundLoader.playSound(Sounds.MELT);
            DungeonDiver7.getStuffBag().getGameLogic();
            GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
        } else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE) {
        }
        return true;
    }

    @Override
    public final int getBaseID() {
        return 127;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
        switch (materialID) {
            case Materials.FIRE:
                if (this.hasPreviousState()) {
                    return this.getPreviousState();
                } else {
                    return new CrystalBlock();
                }
            default:
                return this;
        }
    }
}