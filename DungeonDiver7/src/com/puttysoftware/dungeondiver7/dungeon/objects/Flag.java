/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractPassThroughObject;

public class Flag extends AbstractPassThroughObject {
    // Constructors
    public Flag() {
	super();
	this.setFrameNumber(1);
    }

    // Scriptability
    @Override
    public boolean defersSetProperties() {
	return false;
    }

    @Override
    public AbstractDungeonObject editorPropertiesHook() {
	return null;
    }

    @Override
    public boolean solvesOnMove() {
	return true;
    }

    @Override
    public int getCustomFormat() {
	return 0;
    }

    @Override
    public final int getBaseID() {
	return 13;
    }
}