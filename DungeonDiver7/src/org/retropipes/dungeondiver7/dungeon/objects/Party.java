/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;

public class Party extends AbstractCharacter {
    public Party(final Direction dir, final int number) {
	super(number);
	this.setDirection(dir);
    }

    // Constructors
    public Party(final int number) {
	super(number);
	this.setDirection(Direction.NORTH);
    }

    @Override
    public void editorPlaceHook(final int x, final int y, final int z) {
	final var me = DungeonDiver7.getStuffBag().getEditor();
	me.setPlayerLocation();
    }

    @Override
    public final int getId() {
	return 36;
    }
}