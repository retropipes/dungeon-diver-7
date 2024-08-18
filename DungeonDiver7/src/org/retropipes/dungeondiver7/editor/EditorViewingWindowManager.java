/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.editor;

import org.retropipes.dungeondiver7.dungeon.Dungeon;

final class EditorViewingWindowManager {
    // Fields
    private static final int VIEWING_WINDOW_SIZE_X = Dungeon.getMinColumns();
    private static final int VIEWING_WINDOW_SIZE_Y = Dungeon.getMinRows();
    private static final int MIN_VIEWING_WINDOW_X = 0;
    private static final int MIN_VIEWING_WINDOW_Y = 0;

    static int getLowerRightViewingWindowLocationX() {
	return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X - 1;
    }

    static int getLowerRightViewingWindowLocationY() {
	return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y - 1;
    }

    static int getMinimumViewingWindowLocationX() {
	return EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
    }

    static int getMinimumViewingWindowLocationY() {
	return EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
    }

    static int getViewingWindowLocationX() {
	return EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
    }

    static int getViewingWindowLocationY() {
	return EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
    }

    static int getViewingWindowSize() {
	return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X;
    }

    static int getViewingWindowSizeX() {
	return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X;
    }

    static int getViewingWindowSizeY() {
	return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y;
    }

    // Constructors
    private EditorViewingWindowManager() {
	// Do nothing
    }
}
