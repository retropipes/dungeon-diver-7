/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;

public abstract class AbstractButtonDoor extends AbstractDungeonObject {
    // Constructors
    protected AbstractButtonDoor() {
	super(true);
	this.type.set(TypeConstants.TYPE_BUTTON_DOOR);
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
    public void editorPlaceHook(final int x, final int y, final int z) {
	final Application app = DungeonDiver7.getApplication();
	app.getDungeonManager().getDungeon().fullScanButtonBind(x, y, z, this);
	app.getEditor().redrawEditor();
    }

    @Override
    public void editorRemoveHook(final int x, final int y, final int z) {
	final Application app = DungeonDiver7.getApplication();
	app.getDungeonManager().getDungeon().fullScanFindButtonLostDoor(z, this);
	app.getEditor().redrawEditor();
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }
}