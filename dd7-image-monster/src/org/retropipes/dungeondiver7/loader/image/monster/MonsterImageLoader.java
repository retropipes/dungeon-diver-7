/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.loader.image.monster;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.DianeImageLoader;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public class MonsterImageLoader {
    public static BufferedImageIcon load(final int index) {
	var filename = Integer.toString(index);
	return DianeImageLoader.load("monster-" + filename, MonsterImageLoader.class //$NON-NLS-1$
		.getResource("/asset/image/monster/" + filename + Strings.fileExtension(FileExtension.IMAGE))); //$NON-NLS-1$
    }

    public static BufferedImageIcon loadFinalBoss() {
	return DianeImageLoader.load("final-boss", MonsterImageLoader.class //$NON-NLS-1$
		.getResource("/asset/image/boss/final" + Strings.fileExtension(FileExtension.IMAGE))); //$NON-NLS-1$
    }
}
