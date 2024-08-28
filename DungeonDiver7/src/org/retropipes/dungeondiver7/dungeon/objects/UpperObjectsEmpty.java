/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.dungeondiver7.dungeon.abc.AbstractPassThroughObject;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public class UpperObjectsEmpty extends AbstractPassThroughObject {
    // Constructors
    public UpperObjectsEmpty() {
    }

    @Override
    public final int getIdValue() {
	return 130;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_UPPER_OBJECTS;
    }
}