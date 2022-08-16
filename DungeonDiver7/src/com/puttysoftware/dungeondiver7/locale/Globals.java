package com.puttysoftware.dungeondiver7.locale;

import java.util.ResourceBundle;

public final class Globals {
    private Globals() {
    }

    public static String untranslated(GlobalsUntranslated item) {
	return ResourceBundle.getBundle("locale.untranslated").getString(Integer.toString(item.ordinal()));
    }
}
