/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractButtonDoor extends GameObject {
    // Constructors
    protected AbstractButtonDoor() {
	super(true);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().getDungeon().fullScanButtonBind(x, y, z, this);
	app.getEditor().redrawEditor();
    }

    @Override
    public void editorRemoveHook(final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().getDungeon().fullScanFindButtonLostDoor(z, this);
	app.getEditor().redrawEditor();
    }

    @Override
    public int getCustomProperty(final int propID) {
	return GameObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}