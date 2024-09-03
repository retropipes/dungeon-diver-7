/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.utility.RangeTypes;

public class MirrorCrystalBlock extends AbstractReactionWall {
    // Constructors
    public MirrorCrystalBlock() {
    }

    @Override
    public GameObject changesToOnExposure(final Material materialID) {
	return switch (materialID) {
	case Material.ICE -> {
	    final var icb = new IcyCrystalBlock();
	    icb.setPreviousStateObject(this);
	    yield icb;
	}
	case Material.FIRE -> new HotCrystalBlock();
	default -> this;
	};
    }

    @Override
    public final int getIdValue() {
	return 26;
    }

    @Override
    public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (rangeType == RangeTypes.BOMB || RangeTypes.getMaterialForRangeType(rangeType) == Material.METALLIC) {
	    DungeonDiver7.getStuffBag().getGameLogic();
	    // Destroy mirror crystal block
	    GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Material.FIRE) {
	    // Heat up mirror crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Material.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	} else if (RangeTypes.getMaterialForRangeType(rangeType) == Material.ICE) {
	    // Freeze mirror crystal block
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Material.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	}
	return true;
    }
}