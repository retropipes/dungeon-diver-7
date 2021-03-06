/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDisruptedObject;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;

public class DisruptedMagneticWall extends AbstractDisruptedObject {
    // Fields
    private int disruptionLeft;
    private static final int DISRUPTION_START = 20;

    // Constructors
    public DisruptedMagneticWall() {
	super();
	this.type.set(TypeConstants.TYPE_PLAIN_WALL);
	this.disruptionLeft = DisruptedMagneticWall.DISRUPTION_START;
	this.activateTimer(1);
	this.setMaterial(MaterialConstants.MATERIAL_MAGNETIC);
    }

    @Override
    public void timerExpiredAction(final int locX, final int locY) {
	this.disruptionLeft--;
	if (this.disruptionLeft == 0) {
	    SoundLoader.playSound(SoundConstants.SOUND_DISRUPT_END);
	    final int z = DungeonDiver7.getApplication().getGameManager().getPlayerManager().getPlayerLocationZ();
	    DungeonDiver7.getApplication().getGameManager().morph(new MagneticWall(), locX, locY, z, this.getLayer());
	} else {
	    this.activateTimer(1);
	}
    }

    @Override
    public final int getStringBaseID() {
	return 50;
    }
}