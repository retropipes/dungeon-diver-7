package com.puttysoftware.dungeondiver7.locale;

import java.util.ResourceBundle;

public final class Strings {
    public static final String EMPTY = "";

    private Strings() {
    }

    public static String difficulty(final Difficulty item) {
	return ResourceBundle.getBundle("locale.difficulty").getString(Integer.toString(item.ordinal()));
    }

    public static String timeTravel(final TimeTravel item) {
	return ResourceBundle.getBundle("locale.timetravel").getString(Integer.toString(item.ordinal()));
    }

    public static String timeTravel(final int index) {
	return ResourceBundle.getBundle("locale.timetravel").getString(Integer.toString(index));
    }

    public static String untranslated(Untranslated item) {
	return ResourceBundle.getBundle("locale.untranslated").getString(Integer.toString(item.ordinal()));
    }
}
