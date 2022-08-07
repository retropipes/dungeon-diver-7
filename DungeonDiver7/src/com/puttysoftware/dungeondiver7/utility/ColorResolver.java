/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;

public class ColorResolver {
    private ColorResolver() {
	// Do nothing
    }

    public static String resolveColorConstantToName(final int dir) {
	String res = null;
	if (dir == ColorConstants.COLOR_BLUE) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_BLUE);
	} else if (dir == ColorConstants.COLOR_GREEN) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_GREEN);
	} else if (dir == ColorConstants.COLOR_GRAY) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_GRAY);
	} else if (dir == ColorConstants.COLOR_MAGENTA) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_MAGENTA);
	} else if (dir == ColorConstants.COLOR_RED) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_RED);
	} else if (dir == ColorConstants.COLOR_CYAN) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_CYAN);
	} else if (dir == ColorConstants.COLOR_WHITE) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_WHITE);
	} else if (dir == ColorConstants.COLOR_YELLOW) {
	    res = LocaleLoader.loadString(LocaleConstants.GENERIC_STRINGS_FILE,
		    LocaleConstants.GENERIC_STRING_COLOR_YELLOW);
	}
	return res;
    }

    public static String resolveColorConstantToImageName(final int dir) {
	return LocaleLoader.loadString(LocaleConstants.COLOR_STRINGS_FILE, dir);
    }
}