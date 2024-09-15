/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.loader.image.item;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.DianeImageLoader;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public class ItemImageLoader {
    public static BufferedImageIcon load(final ItemImageId baseId, final int power) {
	var filename = baseId.toString().toLowerCase() + Strings.LOADER_DELIM + Integer.toString(power);
	return DianeImageLoader.load(filename, ItemImageLoader.class
		.getResource("/asset/image/item/" + filename + Strings.fileExtension(FileExtension.IMAGE))); //$NON-NLS-1$
    }
}
