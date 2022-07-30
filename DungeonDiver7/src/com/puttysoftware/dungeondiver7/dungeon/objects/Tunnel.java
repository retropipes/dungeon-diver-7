/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractTunnel;
import com.puttysoftware.dungeondiver7.utilities.ColorConstants;

public class Tunnel extends AbstractTunnel {
    // Constructors
    public Tunnel() {
	super();
	this.setColor(ColorConstants.COLOR_GRAY);
    }

    public Tunnel(final int color) {
	super();
	this.setColor(color);
    }

    @Override
    public final int getStringBaseID() {
	return 44;
    }
}