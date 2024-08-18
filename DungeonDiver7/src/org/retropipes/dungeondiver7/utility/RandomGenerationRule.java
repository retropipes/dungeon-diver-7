/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.utility;

import org.retropipes.dungeondiver7.dungeon.Dungeon;

public interface RandomGenerationRule {
    int NO_LIMIT = 0;

    int getMaximumRequiredQuantity(Dungeon dungeon);

    int getMinimumRequiredQuantity(Dungeon dungeon);

    boolean isRequired(Dungeon dungeon);

    boolean shouldGenerateObject(Dungeon dungeon, int row, int col, int level, int layer);
}
