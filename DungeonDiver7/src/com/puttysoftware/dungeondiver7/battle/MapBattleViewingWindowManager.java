/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle;

import com.puttysoftware.dungeondiver7.prefs.Prefs;

public class MapBattleViewingWindowManager {
    // Fields
    private int oldLocX, oldLocY, locX, locY;
    private static final int VIEWING_WINDOW_SIZE_MULTIPLIER = 1;
    private static final int VIEWING_WINDOW_SIZE_ADDITION = 0;

    // Constructors
    public MapBattleViewingWindowManager() {
        this.locX = 0;
        this.locY = 0;
        this.oldLocX = 0;
        this.oldLocY = 0;
    }

    public int getViewingWindowLocationX() {
        return this.locX;
    }

    public int getViewingWindowLocationY() {
        return this.locY;
    }

    public int getLowerRightViewingWindowLocationX() {
        return this.locX + MapBattleViewingWindowManager.getViewingWindowSize() - 1;
    }

    public int getLowerRightViewingWindowLocationY() {
        return this.locY + MapBattleViewingWindowManager.getViewingWindowSize() - 1;
    }

    public void setViewingWindowCenterX(final int val) {
        this.locX = val - MapBattleViewingWindowManager.getViewingWindowSize() / 2;
    }

    public void setViewingWindowCenterY(final int val) {
        this.locY = val - MapBattleViewingWindowManager.getViewingWindowSize() / 2;
    }

    public void offsetViewingWindowLocationX(final int val) {
        this.locX += val;
    }

    public void offsetViewingWindowLocationY(final int val) {
        this.locY += val;
    }

    public void saveViewingWindow() {
        this.oldLocX = this.locX;
        this.oldLocY = this.locY;
    }

    public void restoreViewingWindow() {
        this.locX = this.oldLocX;
        this.locY = this.oldLocY;
    }

    public static int getViewingWindowSize() {
        return Prefs.getViewingWindowSize() * MapBattleViewingWindowManager.VIEWING_WINDOW_SIZE_MULTIPLIER
                + MapBattleViewingWindowManager.VIEWING_WINDOW_SIZE_ADDITION;
    }
}
