/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.loader.image.status;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.DianeImageLoader;

public class StatusImageLoader {
    public static BufferedImageIcon load(final StatusImageId baseId) {
	return DianeImageLoader.load(baseId);
    }
}
