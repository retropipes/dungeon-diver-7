/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.utility;

import org.retropipes.dungeondiver7.dungeon.base.DungeonBase;

public interface RandomGenerationRule {
    int NO_LIMIT = 0;

    int getMaximumRequiredQuantity(DungeonBase dungeonBase);

    int getMinimumRequiredQuantity(DungeonBase dungeonBase);

    boolean isRequired(DungeonBase dungeonBase);

    boolean shouldGenerateObject(DungeonBase dungeonBase, int row, int col, int level, int layer);
}
