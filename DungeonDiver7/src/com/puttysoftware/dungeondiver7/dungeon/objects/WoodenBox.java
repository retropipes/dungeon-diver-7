/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.MaterialConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public class WoodenBox extends AbstractMovableObject {
    // Constructors
    public WoodenBox() {
	super(true);
	this.type.set(TypeConstants.TYPE_BOX);
	this.setMaterial(MaterialConstants.MATERIAL_WOODEN);
    }

    @Override
    public void playSoundHook() {
	SoundLoader.playSound(SoundConstants.SOUND_PUSH_BOX);
    }

    @Override
    public final int getStringBaseID() {
	return 70;
    }

    @Override
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	switch (materialID) {
	case MaterialConstants.MATERIAL_ICE:
	    final IcyBox ib = new IcyBox();
	    ib.setPreviousState(this);
	    return ib;
	case MaterialConstants.MATERIAL_FIRE:
	    return new Ground();
	default:
	    return this;
	}
    }

    @Override
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	final Application app = DungeonDiver7.getApplication();
	if (forceUnits >= this.getMinimumReactionForce()) {
	    final AbstractDungeonObject mof = app.getDungeonManager().getDungeon().getCell(locX + dirX, locY + dirY,
		    locZ, this.getLayer());
	    final AbstractDungeonObject mor = app.getDungeonManager().getDungeon().getCell(locX - dirX, locY - dirY,
		    locZ, this.getLayer());
	    if (laserType == ArrowTypeConstants.LASER_TYPE_MISSILE) {
		// Destroy wooden box
		SoundLoader.playSound(SoundConstants.SOUND_BARREL);
		app.getGameManager().morph(new Empty(), locX, locY, locZ, this.getLayer());
	    } else if (laserType == ArrowTypeConstants.LASER_TYPE_BLUE && mor != null
		    && (mor.isOfType(TypeConstants.TYPE_CHARACTER) || !mor.isSolid())) {
		app.getGameManager().updatePushedPosition(locX, locY, locX - dirX, locY - dirY, this);
		this.playSoundHook();
	    } else if (mof != null && (mof.isOfType(TypeConstants.TYPE_CHARACTER) || !mof.isSolid())) {
		app.getGameManager().updatePushedPosition(locX, locY, locX + dirX, locY + dirY, this);
		this.playSoundHook();
	    } else {
		// Object doesn't react to this type of laser
		return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	    }
	} else {
	    // Not enough force
	    return super.laserEnteredAction(locX, locY, locZ, dirX, dirY, laserType, forceUnits);
	}
	return Direction.NONE;
    }
}