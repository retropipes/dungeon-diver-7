/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTunnel;
import com.puttysoftware.dungeondiver7.locale.Colors;

public class Tunnel extends AbstractTunnel {
    // Constructors
    public Tunnel() {
	super();
	this.setColor(Colors.GRAY);
    }

    public Tunnel(final Colors color) {
	super();
	this.setColor(color);
    }

    @Override
    public final int getBaseID() {
	return 44;
    }
}