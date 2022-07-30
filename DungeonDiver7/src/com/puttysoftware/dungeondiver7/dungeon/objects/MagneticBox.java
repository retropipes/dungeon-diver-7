/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class MagneticBox extends AbstractMovableObject {
    // Constructors
    public MagneticBox() {
	super(true);
	this.type.set(TypeConstants.TYPE_BOX);
	this.type.set(TypeConstants.TYPE_MAGNETIC_BOX);
	this.setMaterial(MaterialConstants.MATERIAL_MAGNETIC);
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Application app = DungeonDiver7.getApplication();
	final AbstractDungeonObject mo = app.getDungeonManager().getDungeon().getCell(locX - dirX, locY - dirY, locZ,
		this.getLayer());
	if (laserType == ArrowTypeConstants.LASER_TYPE_BLUE && mo != null
		&& (mo.isOfType(TypeConstants.TYPE_CHARACTER) || !mo.isSolid())) {
	    app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
	    this.playSoundHook();
	} else if (mo != null && (mo.isOfType(TypeConstants.TYPE_CHARACTER) || !mo.isSolid())) {
	    app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
	    this.playSoundHook();
	} else {
	    if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
		SoundLoader.playSound(SoundConstants.SOUND_BOOM);
	    } else {
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	}
	return Direction.NONE;
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.SOUND_PUSH_BOX);
    }

    @Override
    public final int getStringBaseID() {
	return 22;
    }
}