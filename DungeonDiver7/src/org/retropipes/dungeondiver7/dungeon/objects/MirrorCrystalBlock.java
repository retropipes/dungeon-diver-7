/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.SoundLoader;
import org.retropipes.dungeondiver7.asset.Sounds;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractReactionWall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.utility.DungeonObjectTypes;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.RangeTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class MirrorCrystalBlock extends AbstractReactionWall {
	// Constructors
	public MirrorCrystalBlock() {
		this.type.set(DungeonObjectTypes.TYPE_PLAIN_WALL);
	}

	@Override
	public AbstractDungeonObject changesToOnExposure(final int materialID) {
		return switch (materialID) {
		case Materials.ICE -> {
			final var icb = new IcyCrystalBlock();
			icb.setPreviousState(this);
			yield icb;
		}
		case Materials.FIRE -> new HotCrystalBlock();
		default -> this;
		};
	}

	@Override
	public boolean doLasersPassThrough() {
		return true;
	}

	@Override
	public final int getBaseID() {
		return 26;
	}

	@Override
	public Direction laserEnteredActionHook(final int locX, final int locY, final int locZ, final int dirX,
			final int dirY, final int laserType, final int forceUnits) {
		return switch (laserType) {
		case ShotTypes.MISSILE -> {
			// Destroy mirror crystal block
			SoundLoader.playSound(Sounds.BOOM);
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(new Empty(), locX, locY, locZ, this.getLayer());
			yield Direction.NONE;
		}
		case ShotTypes.BLUE -> /* Pass laser through */ DirectionResolver.resolve(dirX, dirY);
		case ShotTypes.DISRUPTOR -> {
			// Disrupt mirror crystal block
			SoundLoader.playSound(Sounds.DISRUPTED);
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(new DisruptedMirrorCrystalBlock(), locX, locY, locZ, this.getLayer());
			yield Direction.NONE;
		}
		default -> /* Reflect laser */ DirectionResolver.resolveInvert(dirX, dirY);
		};
	}

	@Override
	public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int laserType) {
		return DirectionResolver.resolve(dirX, dirY);
	}

	@Override
	public boolean rangeActionHook(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
			final int rangeType, final int forceUnits) {
		if (rangeType == RangeTypes.BOMB || RangeTypes.getMaterialForRangeType(rangeType) == Materials.METALLIC) {
			DungeonDiver7.getStuffBag().getGameLogic();
			// Destroy mirror crystal block
			GameLogic.morph(new Empty(), locX + dirX, locY + dirY, locZ, this.getLayer());
			return true;
		}
		if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE) {
			// Heat up mirror crystal block
			SoundLoader.playSound(Sounds.MELT);
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
		} else if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE) {
			// Freeze mirror crystal block
			SoundLoader.playSound(Sounds.FROZEN);
			DungeonDiver7.getStuffBag().getGameLogic();
			GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
		}
		return true;
	}
}