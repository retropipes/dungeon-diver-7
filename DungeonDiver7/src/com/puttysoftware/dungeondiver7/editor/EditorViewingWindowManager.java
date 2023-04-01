/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;

final class EditorViewingWindowManager {
    // Fields
    private static final int VIEWING_WINDOW_SIZE_X = AbstractDungeon.getMinColumns();
    private static final int VIEWING_WINDOW_SIZE_Y = AbstractDungeon.getMinRows();
    private static final int MIN_VIEWING_WINDOW_X = 0;
    private static final int MIN_VIEWING_WINDOW_Y = 0;

    // Constructors
    private EditorViewingWindowManager() {
        // Do nothing
    }

    // Methods
    static int getViewingWindowLocationX() {
        return EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
    }

    static int getViewingWindowLocationY() {
        return EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
    }

    static int getLowerRightViewingWindowLocationX() {
        return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_X - 1;
    }

    static int getLowerRightViewingWindowLocationY() {
        return EditorViewingWindowManager.VIEWING_WINDOW_SIZE_Y - 1;
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

    static int getMinimumViewingWindowLocationX() {
        return EditorViewingWindowManager.MIN_VIEWING_WINDOW_X;
    }

    static int getMinimumViewingWindowLocationY() {
        return EditorViewingWindowManager.MIN_VIEWING_WINDOW_Y;
    }
}
