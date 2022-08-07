/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;

public interface RandomGenerationRule {
    public static final int NO_LIMIT = 0;

    public boolean shouldGenerateObject(CurrentDungeon dungeon, int row, int col, int level, int layer);

    public int getMinimumRequiredQuantity(CurrentDungeon dungeon);

    public int getMaximumRequiredQuantity(CurrentDungeon dungeon);

    public boolean isRequired(CurrentDungeon dungeon);
}
