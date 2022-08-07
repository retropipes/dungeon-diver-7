/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.utility;

import com.puttysoftware.dungeondiver7.integration1.dungeon.Dungeon;

public interface RandomGenerationRule {
    public static final int NO_LIMIT = 0;

    public boolean shouldGenerateObject(Dungeon dungeon, int row, int col, int level, int layer);

    public int getMinimumRequiredQuantity(Dungeon dungeon);

    public int getMaximumRequiredQuantity(Dungeon dungeon);

    public boolean isRequired(Dungeon dungeon);
}
