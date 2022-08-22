/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;

public interface RandomGenerationRule {
    int NO_LIMIT = 0;

    boolean shouldGenerateObject(AbstractDungeon dungeon, int row, int col, int level, int layer);

    int getMinimumRequiredQuantity(AbstractDungeon dungeon);

    int getMaximumRequiredQuantity(AbstractDungeon dungeon);

    boolean isRequired(AbstractDungeon dungeon);
}
