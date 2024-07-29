/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.utility;

import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;

public interface RandomGenerationRule {
    int NO_LIMIT = 0;

    int getMaximumRequiredQuantity(AbstractDungeon dungeon);

    int getMinimumRequiredQuantity(AbstractDungeon dungeon);

    boolean isRequired(AbstractDungeon dungeon);

    boolean shouldGenerateObject(AbstractDungeon dungeon, int row, int col, int level, int layer);
}
