/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public abstract class AbstractButton extends GameObject {
    // Constructors
    protected AbstractButton(final GameObject boundTo, final boolean isUniversal) {
	super(boundTo, isUniversal);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final var app = DungeonDiver7.getStuffBag();
	final var loc = app.getDungeonManager().getDungeon().findObject(z, this.getBoundObject().getName());
	if (loc != null) {
	    this.setBoundObjectX(loc[0]);
	    this.setBoundObjectY(loc[1]);
	    this.setTriggered(false);
	}
	if (this instanceof AbstractTriggerButton || this instanceof AbstractPressureButton) {
	    app.getDungeonManager().getDungeon().fullScanButtonCleanup(x, y, z, this);
	}
	app.getEditor().redrawEditor();
    }

    @Override
    public int getCustomFormat() {
	return 3;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return switch (propID) {
	case 1 -> this.getBoundObjectX();
	case 2 -> this.getBoundObjectY();
	case 3 -> this.isTriggered() ? 1 : 0;
	default -> GameObject.DEFAULT_CUSTOM_VALUE;
	};
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
    public abstract boolean pushIntoAction(final GameObject pushed, final int x, final int y, final int z);

    @Override
    public abstract void pushOutAction(final GameObject pushed, final int x, final int y, final int z);

    @Override
    public void setCustomProperty(final int propID, final int value) {
	switch (propID) {
	case 1:
	    this.setBoundObjectX(value);
	    break;
	case 2:
	    this.setBoundObjectY(value);
	    break;
	case 3:
	    this.setTriggered(value == 1);
	    break;
	default:
	    break;
	}
    }
}
