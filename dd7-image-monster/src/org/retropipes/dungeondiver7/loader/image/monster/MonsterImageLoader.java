/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.loader.image.monster;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.DianeImageLoader;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public class MonsterImageLoader {

	public static BufferedImageIcon load(final int index) {
		var filename = Integer.toString(index);
		return DianeImageLoader.load(filename, MonsterImageLoader.class
				.getResource("/asset/image/monster/" + filename + Strings.fileExtension(FileExtension.IMAGE)));
	}
}
