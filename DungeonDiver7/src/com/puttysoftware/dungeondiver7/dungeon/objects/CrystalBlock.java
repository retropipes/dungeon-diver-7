/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.diane.direction.DirectionResolver;
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

public class CrystalBlock extends AbstractReactionWall {
    // Constructors
    public CrystalBlock() {
        this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
    }

    @Override
    public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
            final int dirY, final int laserType, final int forceUnits) {
        switch (laserType) {
            case ShotTypes.MISSILE:
                // Destroy crystal block
                SoundLoader.playSound(Sounds.BOOM);
                DungeonDiver7.getStuffBag().getGameLogic();
                GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
                return Direction.NONE;
            case ShotTypes.BLUE:
                // Reflect laser
                return DirectionResolver.resolveInvert(dirX, dirY);
            case ShotTypes.DISRUPTOR:
                // Disrupt crystal block
                SoundLoader.playSound(Sounds.DISRUPTED);
                DungeonDiver7.getStuffBag().getGameLogic();
                GameLogic.morph(new DisruptedCrystalBlock(), locX, locY, locZ, this.getLayer());
                return Direction.NONE;
            default:
                // Pass laser through
                return DirectionResolver.resolve(dirX, dirY);
        }
    }

    @Override
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final int laserType) {
        return DirectionResolver.resolve(dirX, dirY);
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
            final int rangeType, final int forceUnits) {
        if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
            DungeonDiver7.getStuffBag().getGameLogic();
            // Destroy crystal block
            GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
            return true;
        }
        if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE) {
            // Heat up crystal block
            SoundLoader.playSound(Sounds.MELT);
            DungeonDiver7.getStuffBag().getGameLogic();
            GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
        } else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE) {
            // Freeze crystal block
            SoundLoader.playSound(Sounds.FROZEN);
            DungeonDiver7.getStuffBag().getGameLogic();
            GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
        }
        return true;
    }

    @Override
    public boolean doLasersPassThrough() {
        return true;
    }

    @Override
    public final int getBaseID() {
        return 10;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
        switch (materialID) {
            case Materials.ICE:
                final var icb = new IcyCrystalBlock();
                icb.setPreviousState(this);
                return icb;
            case Materials.FIRE:
                return new HotCrystalBlock();
            default:
                return this;
        }
    }
}