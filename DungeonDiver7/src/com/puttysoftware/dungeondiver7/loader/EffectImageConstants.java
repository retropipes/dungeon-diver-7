/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.dungeondiver7.locale.Strings;

public class EffectImageConstants {
    public static final int EFFECT_IMAGE_ATTACK = 0;
    public static final int EFFECT_IMAGE_DEFENSE = 1;
    public static final int EFFECT_IMAGE_GOLD = 6;
    public static final int EFFECT_IMAGE_HEALTH = 3;
    public static final int EFFECT_IMAGE_MAGIC = 5;
    public static final int EFFECT_IMAGE_NAME = 7;
    public static final int EFFECT_IMAGE_XP = 2;
    public static final int EFFECT_IMAGE_LEVEL = 4;

    static String getEffectImageName(final int ID) {
	return Strings.effectImage(ID);
    }
}
