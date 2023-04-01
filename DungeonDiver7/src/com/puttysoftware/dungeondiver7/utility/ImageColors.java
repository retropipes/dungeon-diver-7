/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.utility;

import java.awt.Color;

public class ImageColors {
    public static final int NONE = -1;
    private static final int COLOR_00 = new Color(127, 127, 255).getRGB();
    private static final int COLOR_01 = new Color(127, 159, 255).getRGB();
    private static final int COLOR_02 = new Color(159, 127, 255).getRGB();
    private static final int COLOR_03 = new Color(159, 159, 255).getRGB();
    private static final int COLOR_04 = new Color(127, 191, 255).getRGB();
    private static final int COLOR_05 = new Color(159, 191, 255).getRGB();
    private static final int COLOR_06 = new Color(191, 191, 255).getRGB();
    private static final int COLOR_07 = new Color(127, 223, 255).getRGB();
    private static final int COLOR_08 = new Color(159, 223, 255).getRGB();
    private static final int COLOR_09 = new Color(191, 223, 255).getRGB();
    private static final int COLOR_10 = new Color(223, 191, 255).getRGB();
    private static final int COLOR_11 = new Color(223, 223, 255).getRGB();
    private static final int COLOR_12 = new Color(0, 255, 255).getRGB();
    private static final int COLOR_13 = new Color(127, 255, 255).getRGB();
    private static final int COLOR_14 = new Color(159, 255, 255).getRGB();
    private static final int COLOR_15 = new Color(191, 255, 255).getRGB();
    private static final int COLOR_16 = new Color(223, 255, 255).getRGB();
    private static final int COLOR_17 = new Color(0, 255, 0).getRGB();
    private static final int COLOR_18 = new Color(0, 255, 63).getRGB();
    private static final int COLOR_19 = new Color(0, 255, 127).getRGB();
    private static final int COLOR_20 = new Color(0, 255, 191).getRGB();
    private static final int COLOR_21 = new Color(63, 255, 0).getRGB();
    private static final int COLOR_22 = new Color(127, 255, 0).getRGB();
    private static final int COLOR_23 = new Color(191, 255, 0).getRGB();
    private static final int COLOR_24 = new Color(63, 255, 63).getRGB();
    private static final int COLOR_25 = new Color(63, 255, 127).getRGB();
    private static final int COLOR_26 = new Color(127, 255, 63).getRGB();
    private static final int COLOR_27 = new Color(127, 255, 127).getRGB();
    private static final int COLOR_28 = new Color(63, 255, 191).getRGB();
    private static final int COLOR_29 = new Color(191, 255, 63).getRGB();
    private static final int COLOR_30 = new Color(255, 255, 0).getRGB();
    private static final int COLOR_31 = new Color(255, 255, 63).getRGB();
    private static final int COLOR_32 = new Color(255, 255, 127).getRGB();
    private static final int COLOR_33 = new Color(255, 255, 191).getRGB();
    private static final int COLOR_34 = new Color(255, 0, 0).getRGB();
    private static final int COLOR_35 = new Color(255, 63, 0).getRGB();
    private static final int COLOR_36 = new Color(255, 127, 0).getRGB();
    private static final int COLOR_37 = new Color(255, 191, 0).getRGB();
    private static final int COLOR_38 = new Color(255, 0, 63).getRGB();
    private static final int COLOR_39 = new Color(255, 0, 127).getRGB();
    private static final int COLOR_40 = new Color(255, 0, 191).getRGB();
    private static final int COLOR_41 = new Color(255, 63, 63).getRGB();
    private static final int COLOR_42 = new Color(255, 63, 127).getRGB();
    private static final int COLOR_43 = new Color(255, 127, 63).getRGB();
    private static final int COLOR_44 = new Color(255, 127, 127).getRGB();
    private static final int COLOR_45 = new Color(255, 63, 191).getRGB();
    private static final int COLOR_46 = new Color(255, 127, 191).getRGB();
    private static final int COLOR_47 = new Color(255, 191, 63).getRGB();
    private static final int COLOR_48 = new Color(255, 191, 127).getRGB();
    private static final int COLOR_49 = new Color(255, 191, 191).getRGB();
    private static final int COLOR_50 = new Color(255, 0, 255).getRGB();
    private static final int COLOR_51 = new Color(255, 63, 255).getRGB();
    private static final int COLOR_52 = new Color(255, 127, 255).getRGB();
    private static final int COLOR_53 = new Color(255, 191, 255).getRGB();
    private static final int COLOR_54 = new Color(255, 255, 255).getRGB();
    private static final int[] LEVEL_COLORS = { ImageColors.COLOR_00, ImageColors.COLOR_01, ImageColors.COLOR_02,
            ImageColors.COLOR_03, ImageColors.COLOR_04, ImageColors.COLOR_05, ImageColors.COLOR_06,
            ImageColors.COLOR_07, ImageColors.COLOR_08, ImageColors.COLOR_09, ImageColors.COLOR_10,
            ImageColors.COLOR_11, ImageColors.COLOR_12, ImageColors.COLOR_13, ImageColors.COLOR_14,
            ImageColors.COLOR_15, ImageColors.COLOR_16, ImageColors.COLOR_17, ImageColors.COLOR_18,
            ImageColors.COLOR_19, ImageColors.COLOR_20, ImageColors.COLOR_21, ImageColors.COLOR_22,
            ImageColors.COLOR_23, ImageColors.COLOR_24, ImageColors.COLOR_25, ImageColors.COLOR_26,
            ImageColors.COLOR_27, ImageColors.COLOR_28, ImageColors.COLOR_29, ImageColors.COLOR_30,
            ImageColors.COLOR_31, ImageColors.COLOR_32, ImageColors.COLOR_33, ImageColors.COLOR_34,
            ImageColors.COLOR_35, ImageColors.COLOR_36, ImageColors.COLOR_37, ImageColors.COLOR_38,
            ImageColors.COLOR_39, ImageColors.COLOR_40, ImageColors.COLOR_41, ImageColors.COLOR_42,
            ImageColors.COLOR_43, ImageColors.COLOR_44, ImageColors.COLOR_45, ImageColors.COLOR_46,
            ImageColors.COLOR_47, ImageColors.COLOR_48, ImageColors.COLOR_49, ImageColors.COLOR_50,
            ImageColors.COLOR_51, ImageColors.COLOR_52, ImageColors.COLOR_53, ImageColors.COLOR_54 };

    public static int getColorForLevel(final int level) {
        return ImageColors.LEVEL_COLORS[level];
    }
}
