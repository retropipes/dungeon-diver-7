/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.editor.DungeonEditor;
import com.puttysoftware.dungeondiver7.utility.Direction;

public class Party extends AbstractCharacter {
    // Constructors
    public Party(final int number) {
	super(number);
	this.setDirection(Direction.NORTH);
    }

    public Party(final Direction dir, final int number) {
	super(number);
	this.setDirection(dir);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final DungeonEditor me = DungeonDiver7.getStuffBag().getEditor();
	me.setPlayerLocation();
    }

    @Override
    public final int getBaseID() {
	return 36;
    }
}