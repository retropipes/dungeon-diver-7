package com.puttysoftware.dungeondiver7.locale;

import java.util.ResourceBundle;

public final class Strings {
    public static final String EMPTY = "";

    private Strings() {
    }

    public static String difficulty(final Difficulty item) {
	return ResourceBundle.getBundle("locale.difficulty").getString(Integer.toString(item.ordinal()));
    }

    public static String direction(final Direction item) {
	return ResourceBundle.getBundle("locale.direction").getString(Integer.toString(item.ordinal()));
    }

    public static String menu(final Menu item) {
	return ResourceBundle.getBundle("locale.menu").getString(Integer.toString(item.ordinal()));
    }

    public static String monster(final int index) {
	return ResourceBundle.getBundle("locale.monster").getString(Integer.toString(index));
    }

    public static String timeTravel(final TimeTravel item) {
	return ResourceBundle.getBundle("locale.timetravel").getString(Integer.toString(item.ordinal()));
    }

    public static String timeTravel(final int index) {
	return ResourceBundle.getBundle("locale.timetravel").getString(Integer.toString(index));
    }

    public static String untranslated(final Untranslated item) {
	return ResourceBundle.getBundle("locale.untranslated").getString(Integer.toString(item.ordinal()));
    }

    public static String zone(final int index) {
	return ResourceBundle.getBundle("locale.zone").getString(Integer.toString(index));
    }

    public static String monsterzone(final int zoneID, final int monID) {
	return Strings.subst(ResourceBundle.getBundle("locale.monsterzone").getString(Integer.toString(0)),
		Strings.zone(zoneID), Strings.monster(monID));
    }

    public static String subst(final String orig, final String... values) {
	String result = orig;
	for (int s = 0; s < values.length; s++) {
	    result = result.replace("%" + s, values[s]);
	}
	return result;
    }
}
