/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovableObject;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public class MagneticBox extends AbstractMovableObject {
    // Constructors
    public MagneticBox() {
	super(true);
	this.setMaterial(Materials.MAGNETIC);
    }

    @Override
    public final int getIdValue() {
	return 22;
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final var app = DungeonDiver7.getStuffBag();
	final var mo = app.getDungeonManager().getDungeon().getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
	if (laserType == ShotTypes.BLUE && mo != null && (mo.isPlayer() || !mo.isSolid())) {
	    app.getGameLogic().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
	    this.playSoundHook();
	} else if (mo != null && (mo.isPlayer() || !mo.isSolid())) {
	    app.getGameLogic().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
	    this.playSoundHook();
	} else if (laserType == ShotTypes.MISSILE) {
	    SoundLoader.playSound(Sounds.KABOOM);
	} else {
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(Sounds.PUSH);
    }
}