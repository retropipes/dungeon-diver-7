/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractTunnel;
import org.retropipes.dungeondiver7.locale.Colors;

public class Tunnel extends AbstractTunnel {
    // Constructors
    public Tunnel() {
	this.setColor(Colors.GRAY);
    }

    public Tunnel(final Colors color) {
	this.setColor(color);
    }

    @Override
    public final int getIdValue() {
	return 44;
    }
}