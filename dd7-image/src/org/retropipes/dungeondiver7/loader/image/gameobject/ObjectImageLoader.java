/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.loader.image.gameobject;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.DianeImageLoader;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public class ObjectImageLoader {
    public static BufferedImageIcon load(final ObjectImageId baseId) {
	return DianeImageLoader.load(baseId);
    }

    public static BufferedImageIcon load(final String name, final int baseId) {
	var filename = Integer.toString(baseId);
	return DianeImageLoader.load(name, ObjectImageLoader.class
		.getResource("/asset/image/object/" + filename + Strings.fileExtension(FileExtension.IMAGE))); //$NON-NLS-1$
    }
}
